package zone.beecas.handler.system;

import java.util.ArrayList;
import java.util.List;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Commands;
import com.beecas.constants.Keys;
import com.beecas.model.roster.Show;
import com.beecas.service.IPSService;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class UserVariableUpdateEventHandler extends BaseServerEventHandler {

    @SuppressWarnings("unchecked")
    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        IPSService psService = BeecasServiceManager.getService(IPSService.class);
        User theUser = (User) event.getParameter(SFSEventParam.USER);
        List<UserVariable> userVars = (List<UserVariable>) event.getParameter(SFSEventParam.VARIABLES);
        int n = userVars.size();
        trace("User variable update " + theUser.getName() + " " + n + " vars");
        for (int i = 0; i < n; i++) {
            UserVariable userVariable = userVars.get(i);
            if (userVariable != null) {
                String name = userVariable.getName();
                if (name.equals(Keys.STATUS)) {
                    int status = userVariable.getIntValue();
                    trace(name + " " + status);
                    List<String> subscribers = psService.publishStatus(theUser.getName(), Show.fromInteger(status), "");
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
                        respond.putUtfString(Keys.USERNAME, theUser.getName());
                        respond.putByte(Keys.STATUS, (byte) status);
                        respond.putUtfString(Keys.STATUS_STRING, "");
                        send(Commands.UPDATE_STATUS, respond, recipients);
                    }
                } else if (name.equals(Keys.STATUS_STRING)) {
                    String statusString = userVariable.getStringValue();
                    trace(name + " " + statusString);
                    List<String> subscribers = psService.publishStatus(theUser.getName(), Show.Online, statusString);
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
                        respond.putUtfString(Keys.USERNAME, theUser.getName());
                        respond.putByte(Keys.STATUS, (byte) Show.Online.value());
                        respond.putUtfString(Keys.STATUS_STRING, statusString);
                        send(Commands.UPDATE_STATUS, respond, recipients);
                    }
                }
            }
        }
    }

}
