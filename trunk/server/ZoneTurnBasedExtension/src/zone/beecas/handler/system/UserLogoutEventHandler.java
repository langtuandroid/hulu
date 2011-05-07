package zone.beecas.handler.system;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class UserLogoutEventHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        trace("User logout");
    }

}
