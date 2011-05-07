package zone.beecas.handler.system;

import java.util.List;

import com.beecas.BeecasServiceManager;
import com.beecas.service.IUserService;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class UserJoinZoneEventHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        User theUser = (User) event.getParameter(SFSEventParam.USER);
        IUserService userService = BeecasServiceManager.getService(IUserService.class);
        List<UserVariable> userInfo = userService.getUserVariableForLogin(theUser.getName());
        if (userInfo != null) {
            getApi().setUserVariables(theUser, userInfo, true, false);
        }
        trace("UserJoinZoneEventHandler Updated user variables: " + theUser.getName());
    }

}
