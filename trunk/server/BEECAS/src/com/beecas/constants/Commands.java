/**
 * 
 */
package com.beecas.constants;
/**
 * Stores commands for communicating between server & client
 */
public final class Commands {
    
    ///////////////////////////////////////////////// FROM CLIENT TO SEVER
    
    /** Delete buddy from buddy list || ignored list */
    public static final String DELETE_BUDDY = "C3";
    /** Ignore buddy from buddy list */
    public static final String IGNORE_BUDDY = "C4";
    /** Unignore buddy from ignored list */
    public static final String UNIGNORE_BUDDY = "C5";
    /** Move buddy to another group in buddy list */
    public static final String MOVE_BUDDY = "C7";
    /** Rename group */
    public static final String RENAME_GROUP = "C8";
    
    ///////////////////////////////////////////////// FROM SERVER TO CLIENT
    
    /** FB updated time to check on client */
    public static final String FB_PROFILE_UPDATED_TIME = "S0";
    /** A profile to update on client */
    public static final String BUDDY_VARIABLES_UPDATE = "S1";
    /** Server sends all custom variables of users in joined room */
    public static final String ROOM_USERS_INFO = "S3";
    /** Server sends result of adding buddy (buddy is available to be added) */
    public static final String ADD_BUDDY_RESULT = "S4";
    
    public static final String UPDATE_STATUS = "S5";
    
    ///////////////////////////////////////////////// FROM BOTH
    
    /** Server requests submitting FB buddy list for constructing buddy list => client sends FB buddy list */
    public static final String SUBMIT_FB_BUDDY_LIST_FIRST = "0";
    /** Client requests buddy list => server sends buddy list */
    public static final String BUDDY_LIST = "1";
    /** Server requests updating FB profile => client sends his FB profile */
    public static final String UPDATE_FB_PROFILE = "2";
    /** Client request image based on ID => server sends image */
    public static final String IMAGE = "3";
    /** Client sends client info => server sends confirmation that client info is valid, so client can keep forward */
    public static final String CLIENT_INFO = "4";
    /** Client sends my buddy request to someone. Server sends someone's buddy request to me */
    public static final String ADD_BUDDY = "5";
    /** Client sends my confirmation to someone's buddy request. Server sends someone's confirmation to my buddy request */
    public static final String ADD_BUDDY_CONFIRM = "6";
    /** Client requests vars => server sends vars with their values */
    public static final String PROFILE = "7";
    /** Client requests room groups => server sends array of room groups for each game id */
    public static final String ROOM_GROUPS = "8";
}