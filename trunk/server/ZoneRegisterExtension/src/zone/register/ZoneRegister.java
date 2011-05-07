package zone.register;

import zone.register.handler.RegisterEventHandler;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class ZoneRegister extends SFSExtension {

    @Override
    public void init() {
        addEventHandler(SFSEventType.USER_LOGIN, RegisterEventHandler.class);
    }

}
