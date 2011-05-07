package zone.beecas.handler.buddy;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Commands;
import com.beecas.constants.Common;
import com.beecas.constants.Keys;
import com.beecas.service.IFriendService;
import com.beecas.service.IUserService;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AddBuddyConfirmHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        IFriendService friendService = BeecasServiceManager.getService(IFriendService.class);
        String addedUsername = user.getName();
        String addingUsername = object.getUtfString(Keys.USERNAME);
        boolean isAccepted = object.getBool(Keys.ADD_BUDDY_CONFIRM);
        if (isAccepted) {
            String groupName = object.getUtfString(Keys.GROUP);
            trace(addedUsername + " accepted " + addingUsername + " to group " + groupName);
            long newVersion = friendService.accept(addedUsername, addingUsername, groupName);
            if (newVersion > 0) {
                IUserService userService = BeecasServiceManager.getService(IUserService.class);
                ISFSObject addedUserInfo = userService.getBuddyInfo(addedUsername);
                ISFSObject addingUserInfo = userService.getBuddyInfo(addingUsername);
                if (addedUserInfo != null && addingUserInfo != null) {
                    ISFSObject respondConfirmToAdding = SFSObject.newInstance();
                    User addingUser = getApi().getUserByName(addingUsername);
                    respondConfirmToAdding.putByte(Keys.ADD_BUDDY_CONFIRM, Common.CONFIRM_BUDDY_ME_ACCEPTED);
                    respondConfirmToAdding.putUtfString(Keys.USERNAME, addedUsername);
                    respondConfirmToAdding.putSFSObject(Keys.ADD_BUDDY_CONFIRM_INFO, addedUserInfo);
                    send(Commands.ADD_BUDDY_CONFIRM, respondConfirmToAdding, addingUser);

                    ISFSObject respondConfirmToAdded = SFSObject.newInstance();
                    respondConfirmToAdded.putByte(Keys.ADD_BUDDY_CONFIRM, Common.CONFIRM_BUDDY_ME_ACCEPTED);
                    respondConfirmToAdded.putUtfString(Keys.USERNAME, addingUsername);
                    respondConfirmToAdded.putSFSObject(Keys.ADD_BUDDY_CONFIRM_INFO, addingUserInfo);
                    send(Commands.ADD_BUDDY_CONFIRM, respondConfirmToAdded, user);
                }

            } else {
                ISFSObject respondConfirmToAdded = SFSObject.newInstance();
                respondConfirmToAdded.putByte(Keys.ADD_BUDDY_CONFIRM, Common.CONFIRM_BUDDY_ME_ERROR);
                send(Commands.ADD_BUDDY_CONFIRM, respondConfirmToAdded, user);
            }
        } else {
            long newVersion = friendService.deny(addedUsername, addingUsername);
            if (newVersion > 0) {
                User addingUser = getApi().getUserByName(addingUsername);
                ISFSObject respondConfirmToAdding = SFSObject.newInstance();
                respondConfirmToAdding.putByte(Keys.ADD_BUDDY_CONFIRM, Common.CONFIRM_BUDDY_ME_REFUSED);
                send(Commands.ADD_BUDDY_CONFIRM, respondConfirmToAdding, addingUser);
            } else {
                ISFSObject respondConfirmToAdded = SFSObject.newInstance();
                respondConfirmToAdded.putByte(Keys.ADD_BUDDY_CONFIRM, Common.CONFIRM_BUDDY_ME_ERROR);
                send(Commands.ADD_BUDDY_CONFIRM, respondConfirmToAdded, user);
            }

        }
    }
}
