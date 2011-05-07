package zone.beecas.handler.system;

import java.util.ArrayList;
import java.util.List;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Commands;
import com.beecas.constants.Common;
import com.beecas.constants.Keys;
import com.beecas.model.LoginResult;
import com.beecas.model.roster.Presentity;
import com.beecas.model.roster.Show;
import com.beecas.service.IPSService;
import com.beecas.service.IUserService;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.bitswarm.sessions.Session;
import com.smartfoxserver.v2.annotations.Instantiation;
import com.smartfoxserver.v2.annotations.Instantiation.InstantiationMode;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

@Instantiation(InstantiationMode.SINGLE_INSTANCE)
public class LoginEventHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        IUserService userService = BeecasServiceManager.getService(IUserService.class);
        String nickname = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
        String encryptedPassword = (String) event.getParameter(SFSEventParam.LOGIN_PASSWORD);
        ISession session = (Session) event.getParameter(SFSEventParam.SESSION);
        ISFSObject customData = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);
        //boolean isAir = false; 
        int clientType = customData.getInt(Keys.CLIENT_TYPE);
        String version = customData.getUtfString(Keys.VERSION);
        byte status = customData.getByte(Keys.STATUS);
        trace("LoginEventHandler called nickname: " + nickname + " encryptedPassword: " + encryptedPassword.toLowerCase() + " customData " + clientType + " " + version + " " + status);
        LoginResult result = null;
        switch (clientType) {
        case Common.CLIENT_BC_DESKTOP_AIR:
            result = userService.login(nickname, encryptedPassword.toLowerCase(), status, version, session);
            break;
        case Common.CLIENT_BC_WEB:
            result = userService.login(nickname, encryptedPassword.toLowerCase(), status, version, session);
            break;
        case Common.CLIENT_FB_DESKTOP_AIR:
            result = userService.loginSocialNetworkUser(nickname, encryptedPassword.toLowerCase(), status, version);
            break;
        case Common.CLIENT_FB_WEB:
            result = userService.loginSocialNetworkUser(nickname, encryptedPassword.toLowerCase(), status, version);
            break;
        default:
            break;
        }
        if (result == LoginResult.Success) {
            //login OK
            IPSService psService = BeecasServiceManager.getService(IPSService.class);
            Presentity presentity = new Presentity();
            presentity.setLastUpdate(System.currentTimeMillis());
            presentity.setBlast("Tao đang online");
            presentity.setShow(Show.fromInteger(status));
            presentity.setUserName(nickname);
            List<String> subscribers = psService.publishOnline(presentity);
            if (subscribers != null) {
                int length = subscribers.size();
                List<User> recipients = new ArrayList<User>();
                for (int j = 0; j < length; j++) {
                    User user = getApi().getUserByName(subscribers.get(j));
                    if (user != null) {
                        recipients.add(user);
                    }
                }
                ISFSObject respond = SFSObject.newInstance();
                respond.putUtfString(Keys.USERNAME, nickname);
                respond.putByte(Keys.STATUS, (byte) status);
                respond.putUtfString(Keys.STATUS_STRING, "Tao đang online");
                send(Commands.UPDATE_STATUS, respond, recipients);
            }
            trace(nickname + " login success");
        } else if (result == LoginResult.InvalidAuthentication) {
            SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_GUEST_NOT_ALLOWED);
            errData.addParameter(Common.LOGIN_ERROR_INVALID_AUTHENTICATION);
            throw new SFSLoginException(Common.LOGIN_ERROR_INVALID_AUTHENTICATION + " " + nickname, errData);
        } else if (result == LoginResult.NotYetRegister) {
            SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_GUEST_NOT_ALLOWED);
            errData.addParameter("Register");
            throw new SFSLoginException("register " + nickname, errData);
        }
    }
}
