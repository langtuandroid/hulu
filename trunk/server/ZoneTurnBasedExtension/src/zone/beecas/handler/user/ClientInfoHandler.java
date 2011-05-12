package zone.beecas.handler.user;

import com.beecas.constants.Commands;
import com.beecas.constants.Keys;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ClientInfoHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        String clientType = object.getUtfString(Keys.CLIENT_TYPE);
        short socialPlatform = object.getShort(Keys.SOCIAL_PLATFORM);
        short vendor = object.getShort(Keys.PUBLISHER);
        String version = object.getUtfString(Keys.VERSION);
        trace(user.getName() + " sent clientType " + clientType + " vendor " + vendor + " version " + version + " socialPlatform " + socialPlatform);
        user.setProperty(Keys.SOCIAL_PLATFORM, socialPlatform);
        ISFSObject respond = SFSObject.newInstance();
        
        send(Commands.CLIENT_INFO, respond, user);
    }

}
