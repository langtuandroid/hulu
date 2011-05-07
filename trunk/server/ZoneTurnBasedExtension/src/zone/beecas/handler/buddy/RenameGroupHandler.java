package zone.beecas.handler.buddy;

import com.beecas.constants.Keys;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RenameGroupHandler extends BaseClientRequestHandler{

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        String username = user.getName();
        String oldGroupName = object.getUtfString(Keys.GROUP);
        String newGroupName = object.getUtfString(Keys.NEW_GROUP);
    }

}
