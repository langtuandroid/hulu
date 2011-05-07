package zone.beecas.handler.buddy;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Keys;
import com.beecas.service.IFriendService;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class IgnoreBuddyHandler extends BaseClientRequestHandler{

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        IFriendService friendService = BeecasServiceManager.getService(IFriendService.class);
        String username = user.getName();
        String blackedUser = object.getUtfString(Keys.USERNAME);
        long newVersion = friendService.addBlacklist(username, blackedUser);
        if (newVersion > 0) {
            
        }
        else {
            
        }
    }

}
