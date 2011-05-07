package zone.beecas.handler.buddy;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Commands;
import com.beecas.constants.Keys;
import com.beecas.service.IFriendService;
import com.beecas.service.IUserService;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AddBuddyHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        IFriendService friendService = BeecasServiceManager.getService(IFriendService.class);
        IUserService userService = BeecasServiceManager.getService(IUserService.class);
        String addingUser = user.getName();
        String addedUser = object.getUtfString(Keys.USERNAME);
        String groupName = object.getUtfString(Keys.GROUP);
        long newVersion = friendService.addFriend(addingUser, addedUser, groupName);
        if (newVersion > 0) {
            trace(addingUser + " add buddy " + addedUser + " success");
            object.putBool(Keys.ADD_BUDDY_RESULT, true);
            User beAddUser = getApi().getUserByName(addedUser);
            if (beAddUser != null) {
                ISFSObject userInfo = userService.getBuddyInfo(addingUser);
                ISFSObject respObject = SFSObject.newInstance();
                respObject.putSFSObject(Keys.PROFILE, userInfo);
                send(Commands.ADD_BUDDY, respObject, beAddUser);
            }
        } else {
            trace(addingUser + " add buddy " + addedUser + " fail");
            object.putBool(Keys.ADD_BUDDY_RESULT, false);
        }
        send(Commands.ADD_BUDDY_RESULT, object, user);
    }

}
