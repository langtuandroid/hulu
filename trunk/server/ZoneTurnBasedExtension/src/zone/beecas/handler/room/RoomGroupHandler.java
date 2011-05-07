package zone.beecas.handler.room;

import java.util.Collection;
import java.util.List;

import com.beecas.constants.Keys;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RoomGroupHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        Collection<Integer> gameIdsArray = object.getIntArray(Keys.GAME_IDS_ARRAY);
        for (Integer integer : gameIdsArray) {
            trace("RoomGroupHandler " + integer);
        }
    }

}
