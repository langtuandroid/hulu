package zone.beecas.handler.buddy;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Keys;
import com.beecas.service.IFriendService;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RemoveBuddyHandler extends BaseClientRequestHandler{

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        IFriendService friendService = BeecasServiceManager.getService(IFriendService.class);
        String username = user.getName();
        String removedUser = object.getUtfString(Keys.USERNAME);
        long newVersion = friendService.removeFriend(username, removedUser);
        if (newVersion > 0) {
            
        }
        else {
            
        }        
    }

}
