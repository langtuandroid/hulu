package zone.beecas.handler.profile;

import java.util.ArrayList;
import java.util.List;

import com.beecas.BeecasServiceManager;
import com.beecas.constants.Keys;
import com.beecas.model.BeecasUser;
import com.beecas.service.IUserService;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class FBProfileUpdateHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject object) {
        String updateTime = object.getUtfString(Keys.UPDATED_TIME);
        String username = user.getName();
        System.out.println("updateTime " + updateTime);
        //        IUserService userService = BeecasServiceManager.getService(IUserService.class);
        //        BeecasUser beecasUser = userService.findUser(username);
        //        if (beecasUser != null) {
        //            List<String> fields = new ArrayList<String>();
        //            fields.add("update_time");
        //            List<Object> values = new ArrayList<Object>();
        //            values.add(updateTime);
        //            userService.update(username, fields, values);
        //        }
    }

}
