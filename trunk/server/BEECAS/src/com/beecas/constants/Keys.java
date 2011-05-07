package com.beecas.constants;

/**
 * Stores keys for getting data from SFSObject
 */
public final class Keys {

    // /////////////////////////////////////////////// FROM CLIENT TO SERVER

    public static final String BUDDY_LIST_CHECKSUM = "buddyListChecksum";

    public static final String VERSION = "version";

    public static final String NEW_GROUP = "newGroup";

    public static final String USERNAME_LIST = "usernameList";

    public static final String CLIENT_TYPE = "clientType";

    public static final String PUBLISHER = "publisher";

    public static final String FB_BUDDY_LIST = "fbBuddyList";

    // /////////////////////////////////////////////// FROM SERVER TO CLIENT

    public static final String PROFILE_UPDATE_TYPE = "profileUpdateType";

    public static final String BUDDY_LIST = "buddyList";

    public static final String LOGIN_MESSAGE = "loginMessage";

    public static final String LOGIN_TYPE = "loginType";

    public static final String ROOM_USERS_INFO = "roomUsersInfo";

    public static final String ADD_BUDDY_RESULT = "addBuddyResult";

    public static final String ADD_BUDDY_CONFIRM_TYPE = "addBuddyConfirmType";

    public static final String ADD_BUDDY_CONFIRM_INFO = "addBuddyConfirmInfo";

    public static final String ROOM_GROUP = "roomGroup";// "_Easy"

    public static final String ROOM_GROUP_DISPLAY_NAME = "roomGroupDisplayName";// "_Easy"

    public static final String ROOM_NAME = "roomName";// "GMR1"

    public static final String ROOM_DISPLAY_NAME = "roomDisplayName";// "Room 1"

    public static final String GAME_NAME = "gameName";// "Gomoku"

    public static final String GAME_ROOM_NAME = "gameRoomName";// "GMT1"

    public static final String GAME_ROOM_DISPLAY_NAMES_ARRAY = "gameRoomDisplayNamesArray";// ["Table 1", "Table 2", "Table 3"]

    public static final String GAME_ROOM_NAMES_ARRAY = "gameRoomNamesArray";//["GMT1", "GMT2", "GMT3"]

    // /////////////////////////////////////////////// BOTH

    // Buddy
    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String FULL_NAME = "fullName";

    public static final String EMAIL = "email";

    public static final String AVATAR = "avatar";

    public static final String AVATAR_FRAMES = "avatarFrames";

    public static final String AVATAR_SEQUENCES = "avatarSequences";

    public static final String GENDER = "gender";

    public static final String BIRTHDAY = "birthday";

    public static final String COUNTRY = "country";

    public static final String UPDATED_TIME = "updatedTime";

    public static final String STATUS = "status";

    public static final String STATUS_STRING = "statusString";

    public static final String MONEY = "money";

    public static final String VIP = "vip";

    public static final String VIP_DETAIL = "vipDetail";

    public static final String LEVEL = "level";

    public static final String LEVEL_SYMBOLS = "levelSymbols";

    public static final String GROUP = "group";

    public static final String SUBSCRIPTION = "subscription";

    public static final String TABLES_JOINING = "tablesJoining";

    public static final String ALL_GAMES = "allGames";// From here to below are game related vars

    public static final String GAME = "game";

    public static final String GAME_TYPE = "gameType";

    public static final String GAME_LEVEL = "gameLevel";

    public static final String GAME_SCORE = "gameScore";

    public static final String GAME_TABLES_PLAYED_NUMBER = "gameTablesPlayedNumber";

    public static final String GAME_TABLES_WON_NUMBER = "gameTablesWonNumber";

    public static final String GAME_TABLES_LOST_NUMBER = "gameTablesLostNumber";

    // FB profile
    public static final String FB_USERNAME = "fbUsername";

    public static final String FB_FULL_NAME = "fbFullName";

    public static final String FB_EMAIL = "fbEmail";

    public static final String FB_GENDER = "fbGender";

    public static final String FB_BIRTHDAY = "fbBirthday";

    public static final String FB_UPDATED_TIME = "fbUpdatedTime";

    public static final String FB_LOCALE = "fbLocale";

    /**
     * Client sends confirmation about adding a buddy => Server sends confirmation to another client
     */
    public static final String ADD_BUDDY_CONFIRM = "addBuddyConfirm";

    public static final String PROFILE = "profile";

    public static final String SOCIAL_PROFILE = "socialProfile";

    public static final String SOCIAL_USERNAME = "socialUsername";

    public static final String SOCIAL_PASSWORD = "socialPassword";

    // /////////////////////////////////////////////// DATABASE

    public static final String BEECAS_TB = "beecas_user";

    public static final String FB_TB = "fb_user";

    public static final String ID_DB = "Id";

    public static final String USERNAME_DB = "User_name";

    public static final String PASSWORD_DB = "Password";

    public static final String FULL_NAME_DB = "Full_name";

    public static final String REGISTER_DATE_DB = "Register_date";

    public static final String REGISTER_SERVICE_DB = "Register_service";

    public static final String EMAIL_DB = "Email";

    public static final String EMAIL_IS_PRIVATE_DB = "Email_is_private";

    public static final String GENDER_DB = "Gender";

    public static final String BIRTHDAY_DB = "Birthday";

    public static final String BIRTHDAY_IS_PRIVATE_DB = "Birthday_is_private";

    public static final String COUNTRY_DB = "Country";

    public static final String COUNTRY_IS_PRIVATE_DB = "Country_is_private";

    public static final String UPDATE_TIME_DB = "Update_time";

    public static final String ACTIVATE_TYPE_DB = "Activate_type";

    public static final String PHONE_DB = "Phone";

    public static final String STATUS_DB = "Status";

    public static final String STATUS_STRING_DB = "Status_string";

    public static final String MONEY_DB = "Money";

    public static final String VIP_DB = "Vip";

    public static final String VIP_DETAIL_DB = "vip_detail";

    public static final String LEVEL_DB = "Level";

    public static final String FB_ID_DB = "Id";

    public static final String FB_BEECAS_ID_DB = "Beecas_id";

    public static final String FB_USERNAME_DB = "User_name";

    public static final String FB_PASSWORD_DB = "Password";

    public static final String FB_FULL_NAME_DB = "Full_name";

    public static final String FB_GENDER_DB = "Gender";

    public static final String FB_BIRTHDAY_DB = "Birthday";

    public static final String FB_LOCALE_DB = "Locale";

    public static final String FB_EMAIL_DB = "Email";

    public static final String FB_UPDATE_TIME_DB = "Update_time";

    public static final String GROUP_DB = "Group_name";

    public static final String SUBSCRIPTION_DB = "Subscription";

    public static final String TABLES_JOINING_DB = "tables_joining"; //not use

    public static final String ROOMS_JOINING_DB = "rooms_joining"; //not use

    public static final String GM_LEVEL_DB = "gm_level";

    public static final String GM_SCORE_DB = "gm_score";

    public static final String GM_TABLES_PLAYED_NUMBER_DB = "Gm_tables_played_number";

    public static final String GM_TABLES_WON_NUMBER_DB = "Gm_tables_won_number";

    public static final String GM_TABLES_LOST_NUMBER_DB = "Gm_tables_lost_number";

    public static final String BT_LEVEL_DB = "bt_level";

    public static final String BT_SCORE_DB = "bt_score";

    public static final String BT_TABLES_PLAYED_NUMBER_DB = "bt_tables_played_number";

    public static final String BT_TABLES_WON_NUMBER_DB = "Bt_tables_won_number";

    public static final String BT_TABLES_LOST_NUMBER_DB = "Bt_tables_lost_number";

    public static final String SHARD_TB = "shard";

    public static final String SHARD_ID_DB = "Shard_id";

    public static final String CONNECTION_STRING_DB = "Connection_string";

    public static final String STATUS_SHARD_DB = "Status";

    public static final String CREATE_DATE_SHARDB = "Created_date";

    public static final String USER_LOOKUP_TB = "user_lookup";

    public static final String USER_ID_LOOKUP_DB = "User_id";

    public static final String SHARD_ID_LOOKUP_DB = "Shard_id";

    public static final String USERNAME_LOOKUP_DB = "User_name";
}
