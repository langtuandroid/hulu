package gomoku.beecas.game;

import com.smartfoxserver.v2.annotations.Instantiation;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class MakeMoveHandler extends BaseClientRequestHandler{

    @Override
    public void handleClientRequest(User arg0, ISFSObject arg1) {
        
    }

}
