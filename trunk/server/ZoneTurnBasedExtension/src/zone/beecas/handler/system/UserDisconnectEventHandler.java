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
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class UserDisconnectEventHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        User user = (User) event.getParameter(SFSEventParam.USER);
        trace("User " + user.getName() + " is disconnected: ");
        IPSService psService = BeecasServiceManager.getService(IPSService.class);
        List<String> subscribers = psService.publishDisconnect(user.getName());
        if (subscribers != null) {
            int length = subscribers.size();
            List<User> recipients = new ArrayList<User>();
            for (int j = 0; j < length; j++) {
                User u = getApi().getUserByName(subscribers.get(j));
                if (user != null) {
                    recipients.add(u);
                }
            }
            ISFSObject respond = SFSObject.newInstance();
            respond.putUtfString(Keys.USERNAME, user.getName());
            respond.putByte(Keys.STATUS, (byte) Show.Offline.value());
            respond.putUtfString(Keys.STATUS_STRING, "");
            send(Commands.UPDATE_STATUS, respond, recipients);
        }
    }

}
