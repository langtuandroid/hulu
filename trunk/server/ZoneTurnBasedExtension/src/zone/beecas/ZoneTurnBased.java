package zone.beecas;

import zone.beecas.handler.buddy.AddBuddyConfirmHandler;
import zone.beecas.handler.buddy.AddBuddyHandler;
import zone.beecas.handler.buddy.ChangeGroupHandler;
import zone.beecas.handler.buddy.FBBuddyHandler;
import zone.beecas.handler.buddy.IgnoreBuddyHandler;
import zone.beecas.handler.buddy.RemoveBuddyHandler;
import zone.beecas.handler.buddy.RenameGroupHandler;
import zone.beecas.handler.buddy.RequestBuddyListHandler;
import zone.beecas.handler.buddy.UnignoreBuddyHandler;
import zone.beecas.handler.image.ImageHandler;
import zone.beecas.handler.profile.FBProfileUpdateHandler;
import zone.beecas.handler.room.PlayerToSpectatorEventHandler;
import zone.beecas.handler.room.RoomGroupHandler;
import zone.beecas.handler.room.RoomVariableUpdateEventHandler;
import zone.beecas.handler.room.SpectatorToPlayerEventHandler;
import zone.beecas.handler.room.UserJoinRoomEventHandler;
import zone.beecas.handler.room.UserLeaveRoomEventHandler;
import zone.beecas.handler.system.LoginEventHandler;
import zone.beecas.handler.system.PrivateMessageEventHandler;
import zone.beecas.handler.system.PublicMessageEventHandler;
import zone.beecas.handler.system.ServerReadyEventHandler;
import zone.beecas.handler.system.UserDisconnectEventHandler;
import zone.beecas.handler.system.UserJoinZoneEventHandler;
import zone.beecas.handler.system.UserLogoutEventHandler;
import zone.beecas.handler.system.UserReconnectionSuccessEventHandler;
import zone.beecas.handler.system.UserReconnectionTryEventHandler;
import zone.beecas.handler.system.UserVariableUpdateEventHandler;
import zone.beecas.handler.user.ClientInfoHandler;

import com.beecas.BeecasServer;
import com.beecas.constants.Commands;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class ZoneTurnBased extends SFSExtension {
    private BeecasServer server;

    @Override
    public void init() {

        server = new BeecasServer();
        //        addRequestHandler("register", RegisterMultiHandler.class);
        addRequestHandler(Commands.SUBMIT_FB_BUDDY_LIST_FIRST, FBBuddyHandler.class);
        addRequestHandler(Commands.UPDATE_FB_PROFILE, FBProfileUpdateHandler.class);
        addRequestHandler(Commands.BUDDY_LIST, RequestBuddyListHandler.class);
        addRequestHandler(Commands.ADD_BUDDY, AddBuddyHandler.class);
        addRequestHandler(Commands.DELETE_BUDDY, RemoveBuddyHandler.class);
        addRequestHandler(Commands.ADD_BUDDY_CONFIRM, AddBuddyConfirmHandler.class);
        addRequestHandler(Commands.IGNORE_BUDDY, IgnoreBuddyHandler.class);
        addRequestHandler(Commands.UNIGNORE_BUDDY, UnignoreBuddyHandler.class);
        addRequestHandler(Commands.MOVE_BUDDY, ChangeGroupHandler.class);
        addRequestHandler(Commands.RENAME_GROUP, RenameGroupHandler.class);
        addRequestHandler(Commands.CLIENT_INFO, ClientInfoHandler.class);
        addRequestHandler(Commands.IMAGE, ImageHandler.class);
        addRequestHandler(Commands.ROOM_GROUPS, RoomGroupHandler.class);

        addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
        addEventHandler(SFSEventType.SERVER_READY, ServerReadyEventHandler.class);
        addEventHandler(SFSEventType.USER_JOIN_ZONE, UserJoinZoneEventHandler.class);
        addEventHandler(SFSEventType.USER_LOGOUT, UserLogoutEventHandler.class);
        addEventHandler(SFSEventType.USER_DISCONNECT, UserDisconnectEventHandler.class);
        addEventHandler(SFSEventType.USER_JOIN_ROOM, UserJoinRoomEventHandler.class);
        addEventHandler(SFSEventType.USER_LEAVE_ROOM, UserLeaveRoomEventHandler.class);
        addEventHandler(SFSEventType.PUBLIC_MESSAGE, PublicMessageEventHandler.class);
        addEventHandler(SFSEventType.PRIVATE_MESSAGE, PrivateMessageEventHandler.class);
        addEventHandler(SFSEventType.USER_RECONNECTION_SUCCESS, UserReconnectionSuccessEventHandler.class);
        addEventHandler(SFSEventType.USER_RECONNECTION_TRY, UserReconnectionTryEventHandler.class);
        addEventHandler(SFSEventType.ROOM_VARIABLES_UPDATE, RoomVariableUpdateEventHandler.class);
        addEventHandler(SFSEventType.USER_VARIABLES_UPDATE, UserVariableUpdateEventHandler.class);
        addEventHandler(SFSEventType.PLAYER_TO_SPECTATOR, PlayerToSpectatorEventHandler.class);
        addEventHandler(SFSEventType.SPECTATOR_TO_PLAYER, SpectatorToPlayerEventHandler.class);
        
//        this.lagSimulationMillis = 100; //lag in milliseconds - simulate latency in sfs2x over the LAN
    }

}
