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
        int clientType = object.getInt(Keys.CLIENT_TYPE);
        short vendor = object.getShort(Keys.PUBLISHER);
        String version = object.getUtfString(Keys.VERSION);
        trace(user.getName() + " sent clientType " + clientType + " vendor " + vendor + " version " + version);
        user.setProperty(Keys.CLIENT_TYPE, clientType);
        send(Commands.CLIENT_INFO, SFSObject.newInstance(), user);
    }

}
