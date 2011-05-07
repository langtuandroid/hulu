package com.beecas.service;

import java.util.List;

import org.joda.time.DateTime;

import com.beecas.model.BeecasUser;
import com.beecas.model.FBUser;
import com.beecas.model.LoginResult;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.UserVariable;

public interface IUserService {
    long getUserId(String userName);

    String getUsername(long userId);

    /**
     * only for beecas
     * 
     * @param username
     *            : 6-50 character
     */
    long createUser(String username, String password, String fullname, String email, int gender, DateTime birthday, String country, int clientType);

    /**
     * only for beecas
     * 
     * @param session
     *            TODO
     */
    LoginResult login(String userName, String password, byte status, String version, ISession session);

    public boolean update(String username, List<String> fields, List<Object> values);

    BeecasUser findUser(String nickname);

    BeecasUser findUser(long userId);

    /**
     * only for FB with specify field
     * 
     * @param email
     *            TODO
     * @param clientType
     *            TODO
     * @param fbUsername
     *            TODO
     * @param signedRequest
     *            TODO
     * @param fbFullname
     *            TODO
     * @param fbEmail
     *            TODO
     * @param fbGender
     *            TODO
     * @param fbBirthday
     *            TODO
     * @param fbLocale
     *            TODO
     * @param fbUpdateTime
     *            TODO
     */
    boolean registerSocialNetworkUser(String username, String password, String email, int clientType, String fbUsername, String signedRequest, String fbFullname, String fbEmail, int fbGender,
            DateTime fbBirthday, String fbLocale, DateTime fbUpdateTime);

    /**
     * only for FB with specify field
     */
    LoginResult loginSocialNetworkUser(String fbUsername, String fbPassword, byte status, String version);

    public boolean updateSocialNetworkUser(String username, String fbUsername, List<String> fields, List<Object> values);

    FBUser findSocialNetworkUser(String fbUsername);

    FBUser findSocialNetworkUser(long beecasId);

    ISFSObject getListUserInfoForJoinRoom(List<User> list, String roomName);

    List<UserVariable> getUserVariableForJoinRoom(String username, String roomName);

    List<UserVariable> getUserVariableForLogin(String username);

    ISFSObject getBuddyInfo(long userId);

    ISFSObject getBuddyInfo(String username);
    
    ISFSObject getUserVariableForProfile(String username, boolean isOwner);
}
