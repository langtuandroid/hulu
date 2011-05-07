package com.beecas.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.joda.time.DateTime;
import org.jredis.JRedis;
import org.jredis.RedisException;
import org.jredis.ri.alphazero.JRedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beecas.constants.Common;
import com.beecas.constants.Keys;
import com.beecas.constants.UserConstants;
import com.beecas.model.BeecasUser;
import com.beecas.model.roster.Blacklist;
import com.beecas.model.roster.FriendList;
import com.beecas.model.roster.Item;
import com.beecas.model.roster.Subscription;
import com.beecas.util.BitConverter;
import com.beecas.util.Utility;
import com.google.inject.Inject;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class FriendService implements IFriendService {
    public static final String DEFAULT_GROUP_NAME = "Friend";

    private IUserService userService;

    private ISubscriberService subcriberService;

    private Hashtable<String, Blacklist> blacklists;

    private List<JRedis> jredisList;

    private final String FRIEND = "friend:";

    private final String VERSION = ":vs";

    private final String GROUP_DEFAULT = "Friends";

    private static Logger log = LoggerFactory.getLogger(UserConstants.BEECAS_LOG);

    @Inject
    private FriendService(IUserService userService, ISubscriberService subcriberService) {
        this.userService = userService;
        this.subcriberService = subcriberService;
        Properties properties = SmartFoxServer.getInstance().getZoneManager().getZoneByName(UserConstants.TURN_BASED_ZONE).getExtension().getConfigProperties();
        jredisList = new ArrayList<JRedis>();
        blacklists = new Hashtable<String, Blacklist>();
        int n = Integer.parseInt(properties.getProperty("friendRedisCount"));
        for (int i = 0; i < n; i++) {
            String[] hostInfos = properties.getProperty("friendRedis" + i).split(":");
            int port = Integer.parseInt(hostInfos[1]);
            jredisList.add(new JRedisClient(hostInfos[0], port));
        }
    }

    // friend:<id>:version <version>
    // friend:<id> <{friendid1, friendid2, .....}>
    // friend:<id>:<friendid> <groupname>:<subsciption>
    @Override
    public FriendList getFriendList(String username) {
        long userId = userService.getUserId(username);
        if (userId > 0) {
            String key = FRIEND + userId;
            String keyVersion = key + VERSION;
            JRedis jredis = getRedis(userId);
            try {
                byte[] versionByte = jredis.get(keyVersion);
                long len = jredis.llen(key);
                if (versionByte == null) {
                    jredis.set(keyVersion, BitConverter.getBytes(0));
                    return null;
                }
                if (len == 0) {
                    return null;
                }
                FriendList friendList = new FriendList();
                Blacklist blacklist = new Blacklist();
                ArrayList<String> subscriber = new ArrayList<String>();
                friendList.setVersion(BitConverter.getInt(versionByte));
                friendList.setUsername(username);
                List<byte[]> value = jredis.lrange(key, 0, len);
                //                List<String> keys = new ArrayList<String>();
                String[] moreKeys = new String[(int) len];
                String theFirstKey = null;
                for (int i = 0; i < len; i++) {
                    long id = BitConverter.getLong(value.get(i));
                    Item item = new Item();
                    item.setUserId(id);
                    friendList.getFriends().add(item);
                    if (i == 0) {
                        theFirstKey = key + ":" + id;
                    } else {
                    }
                    moreKeys[i] = key + ":" + id;
                }
                List<byte[]> friends = jredis.mget(theFirstKey, moreKeys);
                for (int i = 0; i < len; i++) {
                    String infor = BitConverter.getString(friends.get(i));
                    int index = infor.indexOf(':');
                    Item item = friendList.getFriends().get(i);
                    item.setGroupName(infor.substring(0, index));
                    int sub = Integer.parseInt(infor.substring(index + 1));
                    Subscription subscription = Subscription.fromInteger(sub);
                    item.setSubscription(subscription);
                    switch (sub) {
                    case 2: // Subscription.From.value(): requested user list
                        String friendname = userService.getUsername(item.getUserId());
                        if (friendname != null) {
                            friendList.getRequests().add(friendname);
                        }
                        break;
                    case 32: // Subscription.Ignore.value():
                        String friendname1 = userService.getUsername(item.getUserId());
                        if (friendname1 != null) {
                            blacklist.addBlacklist(friendname1);
                        }
                        break;
                    case 8: // Subscription.Both.value():
                        String friendname2 = userService.getUsername(item.getUserId());
                        if (friendname2 != null) {
                            subscriber.add(friendname2);
                        }
                        break;
                    default:
                        break;
                    }
                }
                if (blacklists.containsKey(username)) {
                    blacklists.remove(username);
                }
                blacklists.put(username, blacklist);
                subcriberService.subscribe(username, subscriber);
                return friendList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public long removeBlacklist(String addingUser, String addedUser) {
        long newVersion = changeSubscription(addingUser, addedUser, Subscription.Remove);
        if (newVersion > 0 && blacklists.contains(addingUser)) {
            blacklists.get(addingUser).RemoveBlacklist(addedUser);
        }
        return newVersion;
    }

    @Override
    public long addBlacklist(String addingUser, String addedUser) {
        long newVersion = changeSubscription(addingUser, addedUser, Subscription.Ignore);
        if (newVersion > 0 && blacklists.contains(addingUser)) {
            blacklists.get(addingUser).addBlacklist(addedUser);
        }
        return newVersion;
    }

    // return -1 : fail;
    // return -2 : duplicate;
    // return newVersion > 0 : ok;
    @Override
    public long addFriend(String addinguser, String addedUser, String groupName) {
        long newVersion = -1;
        long addingId = userService.getUserId(addinguser);
        long addedId = userService.getUserId(addedUser);
        JRedis addingRedis = getRedis(addingId);
        JRedis addedRedis = getRedis(addingId);
        if (addingId < 0 || addedId < 0) {
            return newVersion;
        }
        String addingKey = FRIEND + addingId;
        String addingFriendKey = addingKey + ":" + addedId;
        try {
            byte[] addingValue = addingRedis.get(addingFriendKey);
            if (addingId == addedId) {
                if (addingValue == null) {
                    addedRedis.rpush(addingKey, BitConverter.getBytes(addingId));
                }
                return updateValueFriend(groupName + ":" + Subscription.Both.value(), addingFriendKey, addingKey + VERSION, addingRedis);
            }
            String addedKey = FRIEND + addedId;
            String addedFriendKey = addedKey + ":" + addingId;
            byte[] addedValue = addedRedis.get(addedFriendKey);
            int both = Subscription.Both.value(), to = Subscription.To.value(), from = Subscription.From.value();
            if (addedValue != null && addingValue != null) {
                String addingstr = BitConverter.getString(addingValue);
                String addedStr = BitConverter.getString(addedValue);
                int addingIndex = addingstr.indexOf(":");
                int addedIndex = addedStr.indexOf(":");
                int addingSub = Integer.parseInt(addingstr.substring(addingIndex + 1));
                int addedSub = Integer.parseInt(addedStr.substring(addedIndex + 1));
                if ((addingSub == both && (addedSub == both)) || (addingSub == from && addedSub == to) || (addingSub == to && addedSub == from)) {
                    return -2;
                } else if ((addedSub == both)) {
                    return updateValueFriend(groupName + ":" + both, addingFriendKey, addingKey + VERSION, addingRedis);
                }
            } else if (addedValue != null) {
                addedRedis.rpush(addedKey, BitConverter.getBytes(addingId));
            } else if (addingValue != null) {
                //                addingRedis.lpush(addingKey, addedId);
                addingRedis.lpush(addingKey, BitConverter.getBytes(addedId));
            } else {
                addedRedis.rpush(addedKey, BitConverter.getBytes(addingId));
                addingRedis.lpush(addingKey, BitConverter.getBytes(addedId));
            }
            updateValueFriend(GROUP_DEFAULT + ":" + from, addedFriendKey, addedKey + VERSION, addedRedis); // update for added
            return updateValueFriend(groupName + ":" + to, addingFriendKey, addingKey + VERSION, addingRedis); // update for adding
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return newVersion;
    }

    @Override
    public long accept(String username, String addingUser, String groupName) {
        long version = -1;
        Subscription newSub = Subscription.Both;
        long addingId = userService.getUserId(addingUser);
        long userId = userService.getUserId(username);
        if (addingId < 0 || userId < 0) {
            return version;
        }
        try {
            JRedis jredis = getRedis(addingId);
            String versionKey = FRIEND + addingId + VERSION;
            String key = FRIEND + addingId + ":" + userId;
            byte[] oldValue = jredis.get(key);
            if (oldValue != null) {
                String oldStr = BitConverter.getString(oldValue);
                int index = oldStr.indexOf(":");
                String groupName1 = oldStr.substring(0, index + 1);
                updateValueFriend(groupName1 + newSub.value(), key, versionKey, jredis); //update for adding
            }
            version = updateValueFriend(groupName + ":" + newSub.value(), FRIEND + userId + ":" + addingId, FRIEND + userId + VERSION, getRedis(userId)); //update for added
        } catch (RedisException e) {
            e.printStackTrace();
        }
        subcriberService.subscribe(username, addingUser);
        subcriberService.subscribe(addingUser, username);
        return version;
    }

    @Override
    public long deny(String username, String addingUser) {
        long newVersion = changeSubscription(username, addingUser, Subscription.None);
        changeSubscription(addingUser, username, Subscription.None);
        return newVersion;
    }

    @Override
    public long changeGroup(String addingUser, String addedUser, String newGroup) {
        long version = -1;
        long addingId = userService.getUserId(addingUser);
        long addedId = userService.getUserId(addedUser);
        if (addingId <= 0 || addedId <= 0) {
            return version;
        }
        try {
            JRedis jredis = getRedis(addingId);
            String versionKey = FRIEND + addedId + VERSION;
            String key = FRIEND + addingId + ":" + addedId;
            byte[] oldValue = jredis.get(key);
            if (oldValue != null) {
                String oldStr = BitConverter.getString(oldValue);
                int index = oldStr.indexOf(":");
                String sub = oldStr.substring(index);
                return updateValueFriend(newGroup + sub, key, versionKey, jredis);
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    public long removeFriend(String user, String removedFriend) {
        long newVersion = changeSubscription(user, removedFriend, Subscription.Remove); //A remove B
        changeSubscription(removedFriend, user, Subscription.Remove); // B must remove A - force
        subcriberService.unSubscribe(user, removedFriend);
        subcriberService.unSubscribe(removedFriend, user);
        return newVersion;
    }

    private long changeSubscription(String addingUser, String addedUser, Subscription newSub) {
        int version = -1;
        long addingId = userService.getUserId(addingUser);
        long addedId = userService.getUserId(addedUser);
        if (addingId < 0 || addedId < 0) {
            return version;
        }
        try {
            JRedis jredis = getRedis(addingId);
            String versionKey = FRIEND + addedId + VERSION;
            String key = FRIEND + addingId + ":" + addedId;
            byte[] oldValue = jredis.get(key);
            if (oldValue != null) {
                String oldStr = BitConverter.getString(oldValue);
                int index = oldStr.indexOf(":");
                String groupName = oldStr.substring(0, index + 1);
                return updateValueFriend(groupName + newSub.value(), key, versionKey, jredis);
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return version;
    }

    private long updateValueFriend(String newValue, String friendKey, String versionKey, JRedis jredis) throws RedisException {
        jredis.set(friendKey, BitConverter.getBytes(newValue));
        byte[] oldVersion = jredis.get(versionKey);
        int n = 0;
        if (oldVersion != null) {
            n = BitConverter.getInt(oldVersion);
            n++;
        }
        jredis.set(versionKey, BitConverter.getBytes(n));
        return n;
        //        return jredis.incr(versionKey);
    }

    private JRedis getRedis(long id) {
        int n = (int) (id % jredisList.size());
        return jredisList.get(n);
    }

    @Override
    public FriendList initBuddyList(String owner, Collection<String> buddyFb, User ownerUser) {
//        try {
//            DateTime birthday = Utility.convertBirthdayStringToDateTime(userInfo.getUtfString(Keys.BIRTHDAY));
//            DateTime updateTime = Utility.convertStringToDateTime(userInfo.getUtfString(Keys.UPDATED_TIME));
//            String userName = userInfo.getUtfString(Keys.USERNAME);
//            String fullName = userInfo.getUtfString(Keys.FULL_NAME);
//            long addingId = userService.createUser(userName, "", fullName, userInfo.getUtfString(Keys.EMAIL), userInfo.getByte(Keys.GENDER), birthday, userInfo.getUtfString(Keys.LOCALE),
//                    UserContants.FACEBOOK, updateTime);
//            if (addingId > 0) {
//                JRedis addingRedis = getRedis(addingId);
//                Iterator<String> iterator = buddyFb.iterator();
//                while (iterator.hasNext()) {
//                    long addedId = userService.getUserId(iterator.next());
//                    if (addedId > 0 && addedId != addingId) {
//                        JRedis addedRedis = getRedis(addingId);
//                        String addingKey = FRIEND + addingId;
//                        String addingFriendKey = addingKey + ":" + addedId;
//                        String addedKey = FRIEND + addedId;
//                        String addedFriendKey = addedKey + ":" + addingId;
//                        addingRedis.rpush(addingKey, BitConverter.getBytes(addedId));
//                        addedRedis.rpush(addedKey, BitConverter.getBytes(addingId));
//                        int both = Subscription.Both.value();
//                        updateValueFriend(GROUP_DEFAULT + ":" + both, addingFriendKey, addingKey + VERSION, addingRedis);
//                        updateValueFriend(GROUP_DEFAULT + ":" + both, addedFriendKey, addedKey + VERSION, addedRedis);
//                    }
//                }
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info(e.toString());
//        }
        return null;
    }
}
