package zone.beecas.handler.room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.beecas.constants.Commands;
import com.beecas.constants.Common;
import com.beecas.constants.Keys;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RoomGroupHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        Collection<Integer> gameIdsArray = object.getIntArray(Keys.GAME_IDS_ARRAY);
        ISFSObject respond = SFSObject.newInstance();
        for (Integer gameId : gameIdsArray) {
            trace("RoomGroupHandler " + gameId);
            ISFSObject game = SFSObject.newInstance();
            if (gameId == Common.GAME_GOMOKU) {
                game.putUtfString(Keys.GAME_NAME, "Gomoku");
            } else if (gameId == Common.GAME_BIG_TWO) {
                game.putUtfString(Keys.GAME_NAME, "Tiến lên");
            }
            List<String> groups = new ArrayList<String>();
            groups.add("_GMEasy");
            groups.add("_GMProfessional");
            groups.add("_GMExpert");
            game.putUtfStringArray(Keys.ROOM_GROUP, groups);
            respond.putSFSObject(gameId.toString(), game);
        }
        send(Commands.ROOM_GROUPS, respond, user);
    }
}
