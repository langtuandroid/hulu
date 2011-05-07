package zone.beecas.handler.room;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class SpectatorToPlayerEventHandler extends BaseServerEventHandler{

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        trace("Spectator to player");
    }

}
