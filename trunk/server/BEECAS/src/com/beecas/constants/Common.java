package com.beecas.constants;

/**
 * Stores common vars for both server & client
 */
public final class Common {
    
    // PUBLISHERS
    public static final int PUBLISHER_BEECAS = 0;
    public static final int PUBLISHER_FPT = 1;
    public static final int PUBLISHER_VTC = 2;
    
    // Client BC
    public static final int CLIENT_BC_WEB = 10000;
    public static final int CLIENT_BC_DESKTOP_AIR = 10001;
    
    // Client FB
    public static final int CLIENT_FB_WEB = 20000;
    public static final int CLIENT_FB_DESKTOP_AIR = 20001;
    
    public static final int LOGIN_ERROR_MESSAGE_INDEX = 33; //Guest users not allowed in Zone: xxx
    public static final String LOGIN_ERROR_INVALID_AUTHENTICATION = "InvalidAuthen";
    
    public static final int ROOM_NORMAL = 0;
    public static final int ROOM_GAME = 1;
    public static final int ROOM_CONFERENCE = 2;
    
    public static final int GAME_GOMOKU = 1;
    public static final int GAME_BIG_TWO = 2;
    public static final int GAME_GUNNY = 3;
    
    public static final byte CONFIRM_BUDDY_ME_ACCEPTED = 0;
    public static final byte CONFIRM_BUDDY_ME_REFUSED = 1;
    public static final byte CONFIRM_BUDDY_ME_ERROR = 2;
    
    public static final int GENDER_UNSPECIFIED = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    
    public static final int STATUS_OFFLINE = 0;
    public static final int STATUS_ONLINE = 1;
    public static final int STATUS_AWAY = 2;
    public static final int STATUS_BUSY = 3;
    public static final int STATUS_INVISIBLE = 4;
    
    /** Server related... */
    public static final int SUBSCRIPTION_NONE = 1;
    /** Buddy is adding me */ 
    public static final int SUBSCRIPTION_FROM = 2;//
    /** I'm adding buddy */
    public static final int SUBSCRIPTION_TO = 4;//
    /** We are buddies */
    public static final int SUBSCRIPTION_BOTH = 8;//
    /** Server related... */
    public static final int SUBSCRIPTION_REMOVE = 16;
    /** Ignored buddy */
    public static final int SUBSCRIPTION_IGNORED = 32;//
    /** Ignored strange buddy */
    public static final int SUBSCRIPTION_IGNORED_STRANGE = 64;//
    
    /** Value of FB birthday if parsing error */
    public static final String PARSING_FB_BIRTHDAY_WARNING = "0000-00-00";
    /** Value of FB updatedTime if parsing error */
    public static final String PARSING_FB_UPDATED_TIME_WARNING = "0000-00-00T00:00:00";
    
    public static final int HIDE_BIRTHDAY = 0;  //default
    public static final int SHOW_FULL_BIRTHDAY = 1;
    public static final int SHOW_ONLY_MONTH_DAY_BIRTHDAY = 2;
    
    public static final int HIDE_COUNTRY = 0;
    public static final int SHOW_COUNTRY  = 1;  //default
    
    public static final int HIDE_EMAIL = 0;  //default
    public static final int SHOW_EMAIL = 1;
}
