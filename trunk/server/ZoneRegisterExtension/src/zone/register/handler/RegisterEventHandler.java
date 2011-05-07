package zone.register.handler;

import org.joda.time.DateTime;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Common;
import com.beecas.constants.Keys;
import com.beecas.constants.UserConstants;
import com.beecas.service.IUserService;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.bitswarm.sessions.Session;
import com.smartfoxserver.v2.api.APIManager;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.util.CryptoUtils;

public class RegisterEventHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        IUserService userService = BeecasServiceManager.getService(IUserService.class);
        String username = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
//        String encryptedPassword = (String) event.getParameter(SFSEventParam.LOGIN_PASSWORD);
        ISFSObject customData = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);
        int clientType = customData.getInt(Keys.CLIENT_TYPE);
        String email = customData.getUtfString(Keys.EMAIL);        
        String encryptedPassword = customData.getUtfString(Keys.PASSWORD);        
        trace("RegisterEventHandler called nickname: " + username + " encryptedPassword: " + encryptedPassword + " customData " + clientType);        
        long id = 0;
        if (clientType == Common.CLIENT_BC_WEB || clientType == Common.CLIENT_BC_DESKTOP_AIR) {
            String fullname = customData.getUtfString(Keys.FULL_NAME);
            byte gender = customData.getByte(Keys.GENDER);
            String birthdayString = customData.getUtfString(Keys.BIRTHDAY);
            DateTime birthday = new DateTime(birthdayString);
            String country = customData.getUtfString(Keys.COUNTRY);
            id = userService.createUser(username, encryptedPassword, fullname, email, gender, birthday, country, clientType);
            if (id > 0) {
                SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_GUEST_NOT_ALLOWED);
                errData.addParameter(username + " register success");
                throw new SFSLoginException("registerSuccess " + username, errData);
            } else {
                SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_GUEST_NOT_ALLOWED);
                errData.addParameter(username + " register fail");
                throw new SFSLoginException("registerFail " + username, errData);
            }
        } else if (clientType == Common.CLIENT_FB_WEB || clientType == Common.CLIENT_FB_DESKTOP_AIR) {
            String signedRequest = customData.getUtfString(Keys.SOCIAL_PASSWORD);
            ISFSObject socialProfile = customData.getSFSObject(Keys.SOCIAL_PROFILE);
            String fbUsername = socialProfile.getUtfString(Keys.FB_USERNAME);
            String fbFullname = socialProfile.getUtfString(Keys.FB_FULL_NAME);
            String fbEmail = socialProfile.getUtfString(Keys.FB_EMAIL);
            byte fbGender = socialProfile.getByte(Keys.FB_GENDER);
            String fbBirthdayString = socialProfile.getUtfString(Keys.FB_BIRTHDAY);
            DateTime fbBirthday = new DateTime(fbBirthdayString);
            String fbLocale = socialProfile.getUtfString(Keys.FB_LOCALE);
            String fbUpdateTimeString = socialProfile.getUtfString(Keys.FB_UPDATED_TIME);
            DateTime fbUpdateTime = new DateTime(fbUpdateTimeString);
            boolean success = userService.registerSocialNetworkUser(username, encryptedPassword, email, clientType, fbUsername, signedRequest, fbFullname, fbEmail, fbGender, fbBirthday, fbLocale, fbUpdateTime);
            if (success) {
                SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_GUEST_NOT_ALLOWED);
                errData.addParameter(username + " register success");
                throw new SFSLoginException("registerSuccess " + username, errData);
            } else {
                SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_GUEST_NOT_ALLOWED);
                errData.addParameter(username + " register fail");
                throw new SFSLoginException("registerFail " + username, errData);
            }
        }
    }

}
