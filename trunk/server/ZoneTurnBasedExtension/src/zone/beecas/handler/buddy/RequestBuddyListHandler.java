package zone.beecas.handler.buddy;

import java.util.List;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Commands;
import com.beecas.constants.Common;
import com.beecas.constants.Keys;
import com.beecas.model.BeecasUser;
import com.beecas.model.roster.FriendList;
import com.beecas.model.roster.Item;
import com.beecas.model.roster.Subscription;
import com.beecas.service.IFriendService;
import com.beecas.service.IUserService;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RequestBuddyListHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        IUserService userService = BeecasServiceManager.getService(IUserService.class);
        int socialPlatform = ((Short) user.getProperty(Keys.SOCIAL_PLATFORM)).intValue();
        if (socialPlatform == Common.SOCIAL_BC) {
            IFriendService friendService = BeecasServiceManager.getService(IFriendService.class);
            FriendList friendList = friendService.getFriendList(user.getName());
            ISFSObject sfsObjectRespondBuddyList = SFSObject.newInstance();
            if (friendList != null) {
                List<Item> buddies = friendList.getFriends();
                int n = buddies.size();
                ISFSArray buddyArray = SFSArray.newInstance();
                for (int i = 0; i < n; i++) {
                    Item buddyItem = buddies.get(i);
                    ISFSObject buddyInfo = userService.getBuddyInfo(buddyItem.getUserId());
                    buddyInfo.putUtfString(Keys.GROUP, buddyItem.getGroupName());
                    buddyInfo.putShort(Keys.SUBSCRIPTION, (short) buddyItem.getSubscription().value());
                    //Add buddyInfo
                    buddyArray.addSFSObject(buddyInfo);
                }
                List<String> requests = friendList.getRequests();
                int m = requests.size();
                for (int i = 0; i < m; i++) {
                    ISFSObject buddyInfo = SFSObject.newInstance();
                    buddyInfo.putUtfString(Keys.USERNAME, requests.get(i));
                    buddyInfo.putShort(Keys.SUBSCRIPTION, (short) Subscription.From.value());
                    buddyArray.addSFSObject(buddyInfo);
                }
                sfsObjectRespondBuddyList.putSFSArray(Keys.BUDDY_LIST, buddyArray);
            } else {
                sfsObjectRespondBuddyList.putSFSArray(Keys.BUDDY_LIST, SFSArray.newInstance());
                send(Commands.BUDDY_LIST, sfsObjectRespondBuddyList, user);
            }
        } else if (socialPlatform == Common.SOCIAL_FB) {
            BeecasUser beecasUser = userService.findUser(user.getName());
            if (beecasUser != null) {
                IFriendService friendService = BeecasServiceManager.getService(IFriendService.class);
                FriendList friendList = friendService.getFriendList(user.getName());
                if (friendList != null) {
                    trace("Hoora, FriendList here!!! " + beecasUser.getEmail());
                    List<Item> buddies = friendList.getFriends();
                    int n = buddies.size();
                    ISFSObject sfsObjectRespondBuddyList = SFSObject.newInstance();
                    ISFSArray buddyArray = SFSArray.newInstance();
                    for (int i = 0; i < n; i++) {
                        Item buddyItem = buddies.get(i);
                        ISFSObject buddyInfo = userService.getBuddyInfo(buddyItem.getUserId());
                        //Add buddyInfo
                        buddyArray.addSFSObject(buddyInfo);
                    }
                    sfsObjectRespondBuddyList.putSFSArray(Keys.BUDDY_LIST, buddyArray);
                    send(Commands.BUDDY_LIST, sfsObjectRespondBuddyList, user);
                }
            } else {
                trace("First time, plz summit your FB Buddy " + user.getName());
                ISFSObject sumitBuddyFirst = SFSObject.newInstance();
                send(Commands.SUBMIT_FB_BUDDY_LIST_FIRST, sumitBuddyFirst, user);
            }
        }
    }
}
