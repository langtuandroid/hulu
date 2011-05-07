package zone.beecas.handler.room;

import java.util.List;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Keys;
import com.beecas.service.IUserService;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.SFSRoom;
import com.smartfoxserver.v2.entities.SFSZone;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class UserJoinRoomEventHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        SFSZone zone = (SFSZone) event.getParameter(SFSEventParam.ZONE);
        SFSRoom room = (SFSRoom) event.getParameter(SFSEventParam.ROOM);
        User user = (User) event.getParameter(SFSEventParam.USER);
        trace("User " + user.getName() + " join room " + room.getName() + " from zone " + zone.getName());
        IUserService userService = BeecasServiceManager.getService(IUserService.class);
        List<UserVariable> userInfo = userService.getUserVariableForJoinRoom(user.getName(), room.getName());
        if (userInfo != null) {
            getApi().setUserVariables(user, userInfo, true, false);
        }
        ISFSObject userDataInRoom = userService.getListUserInfoForJoinRoom(room.getUserList(), room.getName());
        send(Keys.ROOM_USERS_INFO, userDataInRoom, user);
        trace("Updated user variables");
    }

}
