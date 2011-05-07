package zone.beecas.handler.buddy;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Keys;
import com.beecas.service.IFriendService;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ChangeGroupHandler extends BaseClientRequestHandler{

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        IFriendService friendService = BeecasServiceManager.getService(IFriendService.class);
        String addingUser = user.getName();
        String changedUser = object.getUtfString(Keys.USERNAME);
        String newGroup = object.getUtfString(Keys.NEW_GROUP);
        long newVersion = friendService.changeGroup(addingUser, changedUser, newGroup);
        if (newVersion > 0) {
            
        }
        else {
            
        }
    }

}
