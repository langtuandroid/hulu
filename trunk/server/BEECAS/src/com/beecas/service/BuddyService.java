package com.beecas.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beecas.constants.Keys;
import com.beecas.constants.UserConstants;
import com.beecas.model.BeecasUser;
import com.beecas.model.roster.Subscription;
import com.beecas.util.Utility;
import com.google.inject.Inject;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.APIManager;
import com.smartfoxserver.v2.api.ISFSBuddyApi;
import com.smartfoxserver.v2.buddylist.Buddy;
import com.smartfoxserver.v2.buddylist.BuddyList;
import com.smartfoxserver.v2.buddylist.BuddyListManager;
import com.smartfoxserver.v2.buddylist.BuddyVariable;
import com.smartfoxserver.v2.buddylist.SFSBuddy;
import com.smartfoxserver.v2.buddylist.SFSBuddyList;
import com.smartfoxserver.v2.buddylist.SFSBuddyVariable;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSBuddyListException;

public class BuddyService implements IBuddyService {

    private IDatabaseService databaseService;

    private IUserService userService;

    private static Logger log = LoggerFactory.getLogger(UserConstants.BEECAS_LOG);

    @Inject
    private BuddyService(IDatabaseService databaseService, IUserService userService) {
        this.databaseService = databaseService;
        this.userService = userService;
    }

    @Override
    public BuddyList loadBuddyList(String owner) {
        log.info("Load BuddyList " + owner);
        long userId = userService.getUserId(owner);
        if (userId >= 0) {
            Connection connection = null;
            PreparedStatement stm = null;
            try {
                try {
                    connection = databaseService.getSFSConnection();
                    stm = connection.prepareStatement("SELECT * FROM roster AS t1 INNER JOIN item AS t2 ON t1.id = t2.roster_id where t1.jid = ?");
                    stm.setString(1, owner);
                    ResultSet result = stm.executeQuery();
                    BuddyListManager buddyListManager = SmartFoxServer.getInstance().getZoneManager().getZoneByName(UserConstants.TURN_BASED_ZONE).getBuddyListManager();
                    BuddyList buddyList = new SFSBuddyList(owner, buddyListManager);
                    while (result.next()) {
                        String buddyName = result.getString("buddy_jid");
                        String groupName = result.getString("group_name");
                        Buddy buddy = new SFSBuddy(buddyName);
                        buddy.setParentBuddyList(buddyList);
                        List<BuddyVariable> variables = getOfflineVariables(buddyName);
                        log.info("begin set variables " + buddyName);
                        buddy.setVariables(variables);
                        log.info("end set variables " + buddyName);
                        buddy.setBlocked(false);
                        try {
                            buddyList.addBuddy(buddy);
                        } catch (SFSBuddyListException localSFSBuddyListException) {
                            System.out.println(localSFSBuddyListException.toString());
                        }
                        log.info("variables isOffline: " + variables.get(0).isOffline());
                        log.info("variables isNull: " + variables.get(0).isNull());
                    }
                    log.info("return buddylist" + owner);
                    return buddyList;
                } finally {
                    if (connection != null) {
                        stm.close();
                        connection.close();
                    }
                }
            } catch (Exception e) {
                log.info(e.toString());
            }
        }

        return null;
    }

    @Override
    public void saveBuddyList(BuddyList buddyList) {

    }

    @Override
    public boolean initBuddyList(String owner, Collection<String> buddyFb, ISFSObject userInfo, User ownerUser) {
        //        DateTime birthday = Utility.convertBirthdayStringToDateTime(userInfo.getUtfString(Keys.BIRTHDAY));
        //        DateTime updateTime = Utility.convertStringToDateTime(userInfo.getUtfString(Keys.UPDATED_TIME));
        //        String userName = userInfo.getUtfString(Keys.USERNAME);
        //        String fullName = userInfo.getUtfString(Keys.FULL_NAME);
        //        long userId = userService.createUser(userName, "", fullName, userInfo.getUtfString(Keys.EMAIL), userInfo.getByte(Keys.GENDER), birthday, userInfo.getUtfString(Keys.LOCALE),
        //                UserConstants.FACEBOOK, updateTime);
        //        if (userId > 0) {
        //            Connection connection = null;
        //            PreparedStatement stm = null;
        //            try {
        //                try {
        //                    APIManager apiManager = SmartFoxServer.getInstance().getAPIManager();
        //                    ISFSBuddyApi api = apiManager.getBuddyApi();
        //                    connection = databaseService.getSFSConnection();
        //                    stm = connection.prepareStatement("insert into roster (jid, checksum) value (?, 0)", Statement.RETURN_GENERATED_KEYS);
        //                    stm.setString(1, userName);
        //                    stm.executeUpdate();
        //                    ResultSet rosterIdResult = stm.getGeneratedKeys();
        //                    if (rosterIdResult.next()) {
        //                        long rosterId = rosterIdResult.getLong(1);
        //                        int n = buddyFb.size();
        //                        if (n > 0) {
        //                            Iterator<String> iterator = buddyFb.iterator();
        //                            //api.initBuddyList(ownerUser, false);      //not need because client call inited before
        //                            while (iterator.hasNext()) {
        //                                String buddyName = iterator.next();
        //                                System.out.println("buddyName " + buddyName);
        //                                BeecasUser beecasUser = userService.findUser(buddyName);
        //                                if (beecasUser != null) {
        //                                    stm = connection.prepareStatement("insert into item (roster_id, buddy_name, buddy_jid, subscription, group_name) value (?, ?, ?, ?, ?)");
        //                                    stm.setLong(1, rosterId);
        //                                    stm.setString(2, beecasUser.getFullname());
        //                                    stm.setString(3, beecasUser.getUsername());
        //                                    Subscription newSub = Subscription.Both;
        //                                    stm.setInt(4, newSub.value());
        //                                    stm.setString(5, "Friends");
        //                                    stm.executeUpdate();
        //
        //                                    long rosterIdBeAdded = 0;
        //                                    stm = connection.prepareStatement("select id from roster where jid = ?");
        //                                    stm.setString(1, buddyName);
        //                                    ResultSet resultSet = stm.executeQuery();
        //                                    while (resultSet.next()) {
        //                                        rosterIdBeAdded = resultSet.getLong("id");
        //                                    }
        //                                    stm = connection.prepareStatement("insert into item (roster_id, buddy_name, buddy_jid, subscription, group_name) value (?, ?, ?, ?, ?)");
        //                                    stm.setLong(1, rosterIdBeAdded);
        //                                    stm.setString(2, fullName);
        //                                    stm.setString(3, userName);
        //                                    newSub = Subscription.Both;
        //                                    stm.setInt(4, newSub.value());
        //                                    stm.setString(5, "Friends");
        //                                    stm.executeUpdate();
        //
        //                                    api.addBuddy(ownerUser, buddyName, false, false, true);
        //                                }
        //                            }
        //                        }
        //                        return true;
        //                    }
        //                } finally {
        //                    if (connection != null) {
        //                        stm.close();
        //                        connection.close();
        //                    }
        //                }
        //            } catch (Exception e) {
        //                e.printStackTrace();
        //                log.debug(e.toString());
        //                log.info(e.toString());
        //            }
        //        }
        return false;
    }

    @Override
    public List<BuddyVariable> getOfflineVariables(String buddyName) {
        //        log.info("getOfflineVariables: " + buddyName);
        //        List<BuddyVariable> variableList = new ArrayList<BuddyVariable>();
        //        BeecasUser buddyUser = userService.findUser(buddyName);
        //        if (buddyUser != null) {
        //            BuddyVariable buddyVariable = new SFSBuddyVariable(Keys.USERNAME, buddyUser.getUsername());
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.USERNAME);
        //            buddyVariable = new SFSBuddyVariable(Keys.FULL_NAME, buddyUser.getFullname());
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.FULL_NAME);
        //            String email = buddyUser.getEmail();
        //            if (email == null) {
        //                email = "";
        //            }
        //            buddyVariable = new SFSBuddyVariable(Keys.EMAIL, email);
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.EMAIL);
        //            buddyVariable = new SFSBuddyVariable(Keys.GENDER, buddyUser.getGender());
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.GENDER);
        //            buddyVariable = new SFSBuddyVariable(Keys.BIRTHDAY, "");
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.BIRTHDAY);
        //            buddyVariable = new SFSBuddyVariable(Keys.COUNTRY, buddyUser.getCountry());
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.COUNTRY);
        //            buddyVariable = new SFSBuddyVariable(Keys.UPDATED_TIME, "");
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.UPDATED_TIME);
        //            buddyVariable = new SFSBuddyVariable(Keys.STATUS, 1);
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.STATUS);
        //            buddyVariable = new SFSBuddyVariable(Keys.STATUS_STRING, "On BEECAS");
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.STATUS_STRING);
        //            buddyVariable = new SFSBuddyVariable(Keys.MONEY, buddyUser.getMoney());
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.MONEY);
        //            buddyVariable = new SFSBuddyVariable(Keys.VIP, buddyUser.getVip());
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.VIP);
        //            buddyVariable = new SFSBuddyVariable(Keys.LEVEL, buddyUser.getLevel());
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.LEVEL);
        //            buddyVariable = new SFSBuddyVariable(Keys.GROUP, "");
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.GROUP);
        //            buddyVariable = new SFSBuddyVariable(Keys.ta, 1234);
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.TABLES_PLAYED_NUMBER);
        //            buddyVariable = new SFSBuddyVariable(Keys.TABLES_WON_NUMBER, 4321);
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.TABLES_WON_NUMBER);
        //            buddyVariable = new SFSBuddyVariable(Keys.TABLES_LOST_NUMBER, 5678);
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.TABLES_LOST_NUMBER);
        //            SFSObject sfsObjectTableJoining = SFSObject.newInstance();
        //            Collection<String> tableJoining = new ArrayList<String>();
        //            tableJoining.add("GM1");
        //            tableJoining.add("GM2");
        //            sfsObjectTableJoining.putUtfStringArray(Keys.TABLES_JOINING, tableJoining);
        //            buddyVariable = new SFSBuddyVariable(Keys.TABLES_JOINING, sfsObjectTableJoining);
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.TABLES_JOINING);
        //            SFSObject sfsObjectRoomJoining = SFSObject.newInstance();
        //            Collection<String> roomJoining = new ArrayList<String>();
        //            roomJoining.add("RM1");
        //            roomJoining.add("RM2");
        //            sfsObjectRoomJoining.putUtfStringArray(Keys.ROOMS_JOINING, roomJoining);
        //            buddyVariable = new SFSBuddyVariable(Keys.ROOMS_JOINING, sfsObjectRoomJoining);
        //            variableList.add(buddyVariable);
        //            log.info("Added: " + Keys.ROOMS_JOINING);
        //        }
        //        return variableList;
        return null;
    }

    @Override
    public int addFriend(String adding, String added, String groupName) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean checkBuddyInited(String owner) {
        long userId = userService.getUserId(owner);
        if (userId >= 0) {
            Connection connection = null;
            PreparedStatement stm = null;
            try {
                try {
                    connection = databaseService.getSFSConnection();
                    stm = connection.prepareStatement("SELECT * FROM roster where jid = ? limit 1");
                    stm.setString(1, owner);
                    ResultSet result = stm.executeQuery();
                    if (result.next()) {
                        return true;
                    }
                } finally {
                    if (connection != null) {
                        stm.close();
                        connection.close();
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return false;
    }

    @Override
    public ISFSObject getBuddyVariables(String buddyName) {
        //        log.info("getBuddyNULLVariables: " + buddyName);
        //        ISFSObject buddyInfo = SFSObject.newInstance();
        //        BeecasUser buddyUser = userService.findUser(buddyName);
        //        if (buddyUser != null) {
        //            buddyInfo.putUtfString(Keys.USERNAME, buddyUser.getUsername());
        //            buddyInfo.putUtfString(Keys.FULL_NAME, buddyUser.getFullname());
        //            String email = buddyUser.getEmail();
        //            if (email == null) {
        //                email = "";
        //            }
        //            buddyInfo.putUtfString(Keys.EMAIL, email);
        //            buddyInfo.putByte(Keys.GENDER, (byte) buddyUser.getGender());
        //            buddyInfo.putUtfString(Keys.BIRTHDAY, buddyUser.getBirthday().toString());
        //            buddyInfo.putUtfString(Keys.COUNTRY, buddyUser.getCountry());
        //            buddyInfo.putUtfString(Keys.UPDATED_TIME, buddyUser.getUpdateTime().toString());
        //            buddyInfo.putByte(Keys.STATUS, (byte) 1);
        //            buddyInfo.putUtfString(Keys.STATUS_STRING, "On BEECAS");
        //            buddyInfo.putLong(Keys.MONEY, buddyUser.getMoney());
        //            buddyInfo.putByte(Keys.VIP, (byte) buddyUser.getVip());
        //            buddyInfo.putShort(Keys.LEVEL, (short) buddyUser.getLevel());
        //            buddyInfo.putUtfString(Keys.GROUP, "Friends");
        //            buddyInfo.putInt(Keys.TABLES_PLAYED_NUMBER, 1234);
        //            buddyInfo.putInt(Keys.TABLES_WON_NUMBER, 4321);
        //            buddyInfo.putInt(Keys.TABLES_LOST_NUMBER, 5678);
        //            Collection<String> tableJoining = new ArrayList<String>();
        //            tableJoining.add("GM1");
        //            tableJoining.add("GM2");
        //            buddyInfo.putUtfStringArray(Keys.TABLES_JOINING, tableJoining);
        //            Collection<String> roomJoining = new ArrayList<String>();
        //            roomJoining.add("RM1");
        //            roomJoining.add("RM2");
        //            buddyInfo.putUtfStringArray(Keys.ROOMS_JOINING, roomJoining);
        //            log.info("RETURN getBuddyVariables: " + buddyName);
        //            return buddyInfo;
        //        }
        return null;
    }

}
