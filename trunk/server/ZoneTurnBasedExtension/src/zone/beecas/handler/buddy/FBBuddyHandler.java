package zone.beecas.handler.buddy;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Commands;
import com.beecas.constants.Keys;
import com.beecas.model.BeecasUser;
import com.beecas.model.roster.FriendList;
import com.beecas.model.roster.Item;
import com.beecas.service.IFriendService;
import com.beecas.service.IUserService;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class FBBuddyHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        IUserService userService = BeecasServiceManager.getService(IUserService.class);
        trace("FBBuddyHandler called");
        Collection<String> buddyFb = object.getUtfStringArray(Keys.FB_BUDDY_LIST);
        int nBuddy = buddyFb.size();
        if (nBuddy > 0) {
            Iterator<String> iterator = buddyFb.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }

        //        trace("Init buddy list SUCCESS => Sending isSubmitted == TRUE");
        //        ISFSObject sfsObjectRespond = SFSObject.newInstance(); //inited buddylist
        //        sfsObjectRespond.putBool(DataKeys.FB_BUDDY_LIST_IS_SUBMITTED, true);
        //        send(Commands.CMD_FB_BUDDY_LIST_SUBMITTED, sfsObjectRespond, user);

        IFriendService friendService = BeecasServiceManager.getService(IFriendService.class);
        FriendList friendList = friendService.initBuddyList(user.getName(), buddyFb, user);
        if (friendList != null) {
            BeecasUser beecasUser = userService.findUser(user.getName());
            trace("Init buddy list SUCCESS => Sending isSubmitted == TRUE " + beecasUser.getEmail());
            ISFSObject sfsObjectRespondBuddyList = SFSObject.newInstance(); //inited buddylist
            trace("Hoora, Inited FriendList, send now!!! " + beecasUser.getEmail());
            List<Item> buddies = friendList.getFriends();
            int n = buddies.size();
            ISFSArray buddyArray = SFSArray.newInstance();
            for (int i = 0; i < n; i++) {
                Item buddyItem = buddies.get(i);
                ISFSObject buddyInfo = userService.getBuddyInfo(buddyItem.getUserId());
                //Add buddyInfo
                if (buddyInfo != null) {
                    buddyArray.addSFSObject(buddyInfo);
                }
            }
            sfsObjectRespondBuddyList.putSFSArray(Keys.BUDDY_LIST, buddyArray);
            send(Commands.BUDDY_LIST, sfsObjectRespondBuddyList, user);
            if (beecasUser != null) {
                ISFSObject sfsObjectRespondUpdateTime = SFSObject.newInstance();
                sfsObjectRespondUpdateTime.putUtfString(Keys.UPDATED_TIME, beecasUser.getUpdateTime().toString());
                send(Commands.FB_PROFILE_UPDATED_TIME, sfsObjectRespondUpdateTime, user);
            }
        } else {
            trace("Init buddy list FAILED");
        }
    }

}
