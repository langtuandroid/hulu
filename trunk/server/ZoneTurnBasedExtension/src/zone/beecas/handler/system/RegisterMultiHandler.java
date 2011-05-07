package zone.beecas.handler.system;

import com.smartfoxserver.v2.annotations.Instantiation;
import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.annotations.Instantiation.InstantiationMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.IClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

@Instantiation(InstantiationMode.SINGLE_INSTANCE)
@MultiHandler
public class RegisterMultiHandler implements IClientRequestHandler{

    @Override
    public SFSExtension getParentExtension() {
        return null;
    }

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        String requestId = object.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID);
        if (requestId.equals("submitForm")) {
            
        }
        else if (requestId.equals("lostPassword")) {
            
        }
    }

    @Override
    public void setParentExtension(SFSExtension arg0) {
        
    }

}
