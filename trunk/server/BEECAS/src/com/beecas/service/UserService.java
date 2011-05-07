package com.beecas.service;

import gnu.trove.map.hash.TLongObjectHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beecas.constants.Common;
import com.beecas.constants.Keys;
import com.beecas.constants.UserConstants;
import com.beecas.model.BeecasUser;
import com.beecas.model.FBUser;
import com.beecas.model.LoginResult;
import com.beecas.model.roster.Show;
import com.beecas.util.Utility;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;

public class UserService implements IUserService {
    private static Logger log = LoggerFactory.getLogger(UserConstants.BEECAS_LOG);

    private TLongObjectHashMap<BeecasUser> beecasUserMap;

    private TLongObjectHashMap<FBUser> fbUserMap;

    private final ICacheService cacheService;

    private final IDatabaseService databaseService;

    private static ISFSApi api = SmartFoxServer.getInstance().getAPIManager().getSFSApi();

    @Inject
    private UserService(ICacheService cacheService, IDatabaseService databaseService) {
        this.cacheService = cacheService;
        this.databaseService = databaseService;
        beecasUserMap = new TLongObjectHashMap<BeecasUser>();
    }

    @Override
    public BeecasUser findUser(String nickname) {
        BeecasUser user = findUserDB(nickname);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public LoginResult login(String userName, String password, byte status, ISession session) {
        if (validateUsername(userName)) {
            BeecasUser beecasUser = findUser(userName);
            if (beecasUser != null) {
                System.out.println("dbpassword " + beecasUser.getPassword());
                if (api.checkSecurePassword(session, beecasUser.getPassword(), password)) {
                    return LoginResult.Success;
                }
            }
            return LoginResult.InvalidAuthentication;
        }
        return LoginResult.UnknowFormat;
    }

    public long createUser(String username, String password, String fullname, String email, int gender, DateTime birthday, String country, int clientType) {
        if (validateUsername(username)) {
            int registerService = 0;
            if (clientType == Common.SOCIAL_BC) {
                registerService = UserConstants.FROM_BEECAS;
            } else if (clientType == Common.SOCIAL_FB) {
                registerService = UserConstants.FROM_FACEBOOK;
            }
            int[] shardId = new int[1];
            long[] userId = new long[1];
            Connection connection = createSignUpInfor(username, shardId, userId);
            if (connection != null) {
                PreparedStatement stm = null;
                try {
                    try {
                        stm = connection.prepareStatement("insert into " + Keys.BEECAS_TB + " (" + Keys.ID_DB + ", " + Keys.USERNAME_DB + ", " + Keys.PASSWORD_DB + ", " + Keys.FULL_NAME_DB + ", "
                                + Keys.EMAIL_DB + ", " + Keys.GENDER_DB + ", " + Keys.BIRTHDAY_DB + ", " + Keys.COUNTRY_DB + ", " + Keys.REGISTER_DATE_DB + ", " + Keys.REGISTER_SERVICE_DB + ", "
                                + Keys.UPDATE_TIME_DB + ") value (?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now())", Statement.RETURN_GENERATED_KEYS);
                        stm.setLong(1, userId[0]);
                        stm.setString(2, username);
                        stm.setString(3, password);
                        stm.setString(4, fullname);
                        stm.setString(5, email);
                        stm.setInt(6, gender);
                        Timestamp sqlBirthday = new Timestamp(birthday.getMillis());
                        stm.setTimestamp(7, sqlBirthday);
                        stm.setString(8, country);
                        stm.setInt(9, registerService);
                        stm.executeUpdate();
                        ResultSet result = stm.getGeneratedKeys();
                        if (result.next()) {
                            return result.getLong(1);
                        }
                    } finally {
                        if (connection != null) {
                            stm.close();
                            connection.close();
                        }
                    }
                } catch (Exception e) {
                    log.error("createUser ", e.toString());
                }
            }
        }
        return -1;
    }

    @Override
    public long getUserId(String userName) {
        BeecasUser user = findUser(userName);
        if (user != null) {
            return user.getId();
        }
        return -1;
    }

    private BeecasUser findUserDB(String username) {
        BeecasUser user = null;
        Connection connection = null;
        Statement stm = null;
        try {
            try {
                connection = databaseService.getConnectionByUsername(username);
                if (connection != null) {
                    stm = connection.createStatement();
                    ResultSet result = stm.executeQuery("select * from " + Keys.BEECAS_TB + " where " + Keys.USERNAME_DB + " = '" + username + "'");
                    while (result.next()) {
                        user = new BeecasUser();
                        user.setId(result.getLong(Keys.ID_DB));
                        user.setUsername(username);
                        user.setPassword(result.getString(Keys.PASSWORD_DB));
                        user.setFullname(result.getString(Keys.FULL_NAME_DB));
                        DateTime birthday = new DateTime(result.getTimestamp(Keys.BIRTHDAY_DB).getTime());
                        user.setBirthday(birthday);
                        user.setEmail(result.getString(Keys.EMAIL_DB));
                        user.setCountry(result.getString(Keys.COUNTRY_DB));
                        user.setGender(result.getInt(Keys.GENDER_DB));
                        user.setRegisterService(result.getInt(Keys.REGISTER_SERVICE_DB));
                        DateTime registerDate = new DateTime(result.getTimestamp(Keys.REGISTER_DATE_DB).getTime());
                        user.setRegisterDate(registerDate);
                        DateTime updateTime = new DateTime(result.getTimestamp(Keys.UPDATE_TIME_DB).getTime()); //not need UTC because of beecas
                        user.setUpdateTime(updateTime);
                        user.setMoney(result.getLong(Keys.MONEY_DB));
                        user.setVip(result.getInt(Keys.VIP_DB));
                        user.setLevel(result.getInt(Keys.LEVEL_DB));
                        user.setPhone(result.getString(Keys.PHONE_DB));
                        user.setActivateType(result.getInt(Keys.ACTIVATE_TYPE_DB));
                        user.setEmailIsPrivate(result.getInt(Keys.EMAIL_IS_PRIVATE_DB));
                        user.setBirthdayIsPrivate(result.getInt(Keys.BIRTHDAY_IS_PRIVATE_DB));
                        user.setCountryIsPrivate(result.getInt(Keys.COUNTRY_IS_PRIVATE_DB));
                        return user;
                    }
                }
            } finally {
                if (connection != null) {
                    stm.close();
                    connection.close();
                }
            }
        } catch (SQLException e) {
            log.error("findUserDB ", e);
        }
        return null;
    }

    private BeecasUser findUserDB(long userId) {
        BeecasUser user = null;
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            try {
                connection = databaseService.getConnectionByUserId(userId);
                stm = connection.prepareStatement("select * from " + Keys.BEECAS_TB + " where " + Keys.ID_DB + " = '" + userId + "'");
                ResultSet result = stm.executeQuery();
                while (result.next()) {
                    user = new BeecasUser();
                    user.setId(result.getLong(Keys.ID_DB));
                    user.setUsername(result.getString(Keys.USERNAME_DB));
                    user.setPassword(result.getString(Keys.PASSWORD_DB));
                    user.setFullname(result.getString(Keys.FULL_NAME_DB));
                    DateTime birthday = new DateTime(result.getTimestamp(Keys.BIRTHDAY_DB).getTime());
                    user.setBirthday(birthday);
                    user.setEmail(result.getString(Keys.EMAIL_DB));
                    user.setCountry(result.getString(Keys.COUNTRY_DB));
                    user.setGender(result.getInt(Keys.GENDER_DB));
                    user.setRegisterService(result.getInt(Keys.REGISTER_SERVICE_DB));
                    DateTime registerDate = new DateTime(result.getTimestamp(Keys.REGISTER_DATE_DB).getTime());
                    user.setRegisterDate(registerDate);
                    DateTime updateTime = new DateTime(result.getTimestamp(Keys.UPDATE_TIME_DB).getTime()); //not need UTC because of beecas
                    user.setUpdateTime(updateTime);
                    user.setMoney(result.getLong(Keys.MONEY_DB));
                    user.setVip(result.getInt(Keys.VIP_DB));
                    user.setLevel(result.getInt(Keys.LEVEL_DB));
                    user.setPhone(result.getString(Keys.PHONE_DB));
                    user.setActivateType(result.getInt(Keys.ACTIVATE_TYPE_DB));
                    return user;
                }
            } finally {
                if (connection != null) {
                    stm.close();
                    connection.close();
                }
            }
        } catch (SQLException e) {
            log.error("findUserDB ", e);
        }
        return null;
    }

    @Override
    public boolean update(String username, List<String> fields, List<Object> values) {
        try {
            if (fields.size() != values.size()) {
                return false;
            }
            int n = fields.size();
            if (n == 0) {
                return true;
            }
            StringBuilder sql = new StringBuilder("update " + Keys.BEECAS_TB + " set ");
            for (int i = 0; i < n - 1; i++) {
                sql.append(fields.get(i) + "=?,");
            }
            sql.append(fields.get(n - 1) + "=? ");
            sql.append(" where " + Keys.USERNAME_DB + " = '" + username + "'");
            Connection connection = null;
            PreparedStatement stm = null;
            try {
                connection = databaseService.getConnectionByUsername(username);
                if (connection != null) {
                    stm = connection.prepareStatement(sql.toString());
                    for (int i = 0; i < n; i++) {
                        stm.setObject(i + 1, values.get(i));
                    }
                    stm.executeUpdate();
                    return true;
                }
            } finally {
                if (connection != null) {
                    stm.close();
                    connection.close();
                }
            }
        } catch (Exception e) {
            log.error("update ", e);
        }
        return false;
    }

    @Override
    public String getUsername(long userId) {
        BeecasUser user = findUser(userId);
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    @Override
    public BeecasUser findUser(long userId) {
        BeecasUser user = findUserDB(userId);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public boolean registerSocialNetworkUser(String username, String password, String email, int clientType, String fbUsername, String signedRequest, String fbFullname, String fbEmail, int fbGender,
            DateTime fbBirthday, String fbLocale, DateTime fbUpdateTime) {
        int registerService = 0;
        if (clientType == Common.SOCIAL_BC) {
            registerService = UserConstants.FROM_BEECAS;
        } else if (clientType == Common.SOCIAL_FB) {
            registerService = UserConstants.FROM_FACEBOOK;
        }
        if (checkSignedRequest(signedRequest)) {
            Connection conn = null;
            PreparedStatement stmt = null;
            int retryCount = 5;
            boolean transactionCompleted = false;
            do {
                try {
                    conn = databaseService.getSFSConnection();
                    conn.setAutoCommit(false);
                    // Okay, at this point, the 'retry-ability' of the
                    // transaction really depends on your application logic,
                    // whether or not you're using autocommit (in this case
                    // not), and whether you're using transacational storage
                    // engines
                    // For this example, we'll assume that it's _not_ safe
                    // to retry the entire transaction, so we set retry
                    // count to 0 at this point
                    // If you were using exclusively transaction-safe tables,
                    // or your application could recover from a connection going
                    // bad in the middle of an operation, then you would not
                    // touch 'retryCount' here, and just let the loop repeat
                    // until retryCount == 0.
                    retryCount = 0;
                    String insertSql = "insert into " + Keys.BEECAS_TB + " (" + Keys.USERNAME_DB + ", " + Keys.PASSWORD_DB + ", " + Keys.FULL_NAME_DB + ", " + Keys.EMAIL_DB + ", " + Keys.GENDER_DB
                            + ", " + Keys.BIRTHDAY_DB + ", " + Keys.COUNTRY_DB + ", " + Keys.REGISTER_DATE_DB + ", " + Keys.REGISTER_SERVICE_DB + ", " + Keys.UPDATE_TIME_DB
                            + ") value (?, ?, ?, ?, ?, ?, ?, now(), ?, now())";
                    stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.setString(3, email); //email to activated
                    stmt.setInt(4, fbGender);
                    Timestamp sqlBirthday = new Timestamp(fbBirthday.getMillis());
                    stmt.setTimestamp(6, sqlBirthday);
                    stmt.setString(7, fbLocale);
                    stmt.setInt(8, registerService);
                    stmt.execute();
                    long beecasId = 0;
                    ResultSet result = stmt.getGeneratedKeys();
                    if (result.next()) {
                        beecasId = result.getLong(1);
                    }

                    insertSql = "insert into " + Keys.FB_TB + " (" + Keys.FB_USERNAME_DB + ", " + Keys.FB_PASSWORD_DB + ", " + Keys.FB_FULL_NAME_DB + ", " + Keys.FB_EMAIL_DB + ", "
                            + Keys.FB_GENDER_DB + ", " + Keys.FB_BIRTHDAY_DB + ", " + Keys.FB_LOCALE_DB + ", " + Keys.FB_BEECAS_ID_DB + ", " + Keys.FB_UPDATE_TIME_DB
                            + ") value (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    stmt = conn.prepareStatement(insertSql);
                    stmt.setString(1, fbUsername);
                    stmt.setString(2, signedRequest);
                    stmt.setString(3, fbFullname);
                    stmt.setString(4, fbEmail);
                    stmt.setInt(5, fbGender);
                    stmt.setTimestamp(6, sqlBirthday);
                    stmt.setString(7, fbLocale);
                    stmt.setLong(8, beecasId);
                    Timestamp sqlUpdateTime = new Timestamp(fbUpdateTime.getMillis());
                    stmt.setTimestamp(9, sqlUpdateTime);
                    stmt.execute();

                    stmt.close();
                    stmt = null;
                    conn.commit();
                    conn.close();
                    conn = null;
                    transactionCompleted = true;
                } catch (SQLException sqlEx) {
                    // The two SQL states that are 'retry-able' are 08S01
                    // for a communications error, and 40001 for deadlock.
                    // Only retry if the error was due to a stale connection,
                    // communications problem or deadlock
                    String sqlState = sqlEx.getSQLState();
                    if ("08S01".equals(sqlState) || "40001".equals(sqlState)) {
                        retryCount--;
                    } else {
                        retryCount = 0;
                    }
                } finally {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException sqlEx) {
                        }
                    }
                    if (conn != null) {
                        try {
                            try {
                                conn.rollback();
                            } finally {
                                conn.close();
                            }
                        } catch (SQLException sqlEx) {
                        }
                    }
                }
            } while (!transactionCompleted && (retryCount > 0));
        }
        return false;
    }

    @Override
    public FBUser findSocialNetworkUser(String fbUsername) {
        FBUser user = findSocialNetworkUserDB("select * from " + Keys.FB_TB + " where " + Keys.FB_USERNAME_DB + " = '" + fbUsername + "'");
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public FBUser findSocialNetworkUser(long beecasId) {
        FBUser user = findSocialNetworkUserDB("select * from " + Keys.FB_TB + " where " + Keys.FB_BEECAS_ID_DB + " = " + beecasId);
        if (user != null) {
            return user;
        }
        return null;
    }

    private FBUser findSocialNetworkUserDB(String sql) {
        FBUser user = null;
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            try {
                connection = databaseService.getSFSConnection();
                stm = connection.prepareStatement(sql);
                ResultSet result = stm.executeQuery();
                while (result.next()) {
                    user = new FBUser();
                    user.setId(result.getLong(Keys.FB_ID_DB));
                    user.setBeecasId(result.getLong(Keys.FB_BEECAS_ID_DB));
                    user.setFbPassword(Keys.FB_USERNAME_DB);
                    user.setFbPassword(result.getString(Keys.FB_PASSWORD_DB));
                    user.setFullname(result.getString(Keys.FB_FULL_NAME_DB));
                    DateTime birthday = new DateTime(result.getTimestamp(Keys.FB_BIRTHDAY_DB).getTime());
                    user.setBirthday(birthday);
                    user.setEmail(result.getString(Keys.FB_EMAIL_DB));
                    user.setLocale(result.getString(Keys.FB_LOCALE_DB));
                    user.setGender(result.getInt(Keys.FB_GENDER_DB));
                    DateTime updateTime = new DateTime(result.getTimestamp(Keys.FB_UPDATE_TIME_DB).getTime(), DateTimeZone.UTC); //must be use UTC because rules of SN
                    user.setUpdateTime(updateTime);
                    return user;
                }
            } finally {
                if (connection != null) {
                    stm.close();
                    connection.close();
                }
            }
        } catch (SQLException e) {
            log.error("findSocialNetworkUserDB ", e);
        }
        return null;
    }

    @Override
    public boolean updateSocialNetworkUser(String username, String fbUsername, List<String> fields, List<Object> values) {
        try {
            if (fields.size() != values.size()) {
                return false;
            }
            int n = fields.size();
            if (n == 0) {
                return true;
            }
            StringBuilder sql = new StringBuilder("update " + Keys.FB_TB + " set ");
            for (int i = 0; i < n - 1; i++) {
                sql.append(fields.get(i) + "=?,");
            }
            sql.append(fields.get(n - 1) + "=? ");
            sql.append(" where " + Keys.FB_USERNAME_DB + " = '" + fbUsername + "'");
            Connection connection = null;
            PreparedStatement stm = null;
            try {
                connection = databaseService.getConnectionByUsername(username);
                if (connection != null) {
                    stm = connection.prepareStatement(sql.toString());
                    for (int i = 0; i < n; i++) {
                        stm.setObject(i + 1, values.get(i));
                    }
                    stm.executeUpdate();
                    return true;
                }
            } finally {
                if (connection != null) {
                    stm.close();
                    connection.close();
                }
            }
        } catch (Exception e) {
            log.error("updateSocialNetworkUser ", e);
        }
        return false;
    }

    @Override
    public LoginResult loginSocialNetworkUser(String fbUsername, String fbPassword, byte status) {
        boolean isValidUser = checkSignedRequest(fbPassword);
        if (isValidUser) {
            boolean isRegister = checkRegister(fbUsername);
            if (isRegister) {
                return LoginResult.Success;
            }
            return LoginResult.NotYetRegister;
        }
        return LoginResult.InvalidAuthentication;
    }

    private boolean checkRegister(String fbUsername) {
        FBUser fbUser = findSocialNetworkUser(fbUsername);
        if (fbUser != null) {
            return true;
        }
        return false;
    }

    private boolean checkSignedRequest(String signedRequest) {
        //test always true
        return true;
    }

    private Connection createSignUpInfor(String username, int[] shardId, long[] userId) {
        try {
            Connection userConnection = databaseService.getActiveConnection(shardId);
            if (shardId[0] > 0) {
                Connection connection = null;
                PreparedStatement stm = null;
                try {
                    connection = databaseService.getSFSConnection();
                    stm = connection.prepareStatement("insert into " + Keys.USER_LOOKUP_TB + " (" + Keys.SHARD_ID_LOOKUP_DB + ", " + Keys.USERNAME_LOOKUP_DB + ") values (?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    stm.setLong(1, shardId[0]);
                    stm.setString(2, username);
                    stm.executeUpdate();
                    ResultSet result = stm.getGeneratedKeys();
                    if (result.next()) {
                        userId[0] = result.getLong(1);
                        return userConnection;
                    }
                } finally {
                    if (connection != null) {
                        stm.close();
                        connection.close();
                    }
                }
            }
        } catch (Exception e) {
            log.error("createSignUpInfor ", e);
        }
        return null;
    }

    private ISFSObject getUserInfoForJoinRoom(long userId, String roomName) {
        try {
            BeecasUser beecasUser = findUser(userId);
            if (beecasUser != null) {
                ISFSObject userInfo = SFSObject.newInstance();
                userInfo.putUtfString(Keys.USERNAME, beecasUser.getUsername());
                userInfo.putUtfString(Keys.FULL_NAME, beecasUser.getFullname());
                ISFSObject avatarObj = SFSObject.newInstance();
                List<Long> frames = new ArrayList<Long>();
                avatarObj.putLongArray(Keys.AVATAR_FRAMES, frames);
                List<Short> sequences = new ArrayList<Short>();
                avatarObj.putShortArray(Keys.AVATAR_SEQUENCES, sequences);
                userInfo.putSFSObject(Keys.AVATAR, avatarObj);
                userInfo.putByte(Keys.GENDER, (byte) beecasUser.getGender());
                userInfo.putByte(Keys.STATUS, (byte) Show.Online.value()); //Get from subcriberservice
                userInfo.putByte(Keys.VIP, (byte) beecasUser.getVip());
                ISFSObject gameInfo = SFSObject.newInstance();
                if (roomName.indexOf("GM") >= 0) {
                    gameInfo.putInt(Keys.GAME_TYPE, Common.GAME_GOMOKU);
                    gameInfo.putUtfString(Keys.GAME_NAME, "Gomoku");
                    gameInfo.putShort(Keys.GAME_LEVEL, (short) 11);
                    gameInfo.putInt(Keys.GAME_SCORE, 1);
                    gameInfo.putInt(Keys.GAME_TABLES_PLAYED_NUMBER, 1);
                    gameInfo.putInt(Keys.GAME_TABLES_WON_NUMBER, 1);
                    gameInfo.putInt(Keys.GAME_TABLES_LOST_NUMBER, 1);
                } else if (roomName.indexOf("BT") > 0) {
                    gameInfo.putInt(Keys.GAME_TYPE, Common.GAME_BIG_TWO);
                    gameInfo.putUtfString(Keys.GAME_NAME, "BigTwo");
                    gameInfo.putShort(Keys.GAME_LEVEL, (short) 22);
                    gameInfo.putInt(Keys.GAME_SCORE, 2);
                    gameInfo.putInt(Keys.GAME_TABLES_PLAYED_NUMBER, 2);
                    gameInfo.putInt(Keys.GAME_TABLES_WON_NUMBER, 2);
                    gameInfo.putInt(Keys.GAME_TABLES_LOST_NUMBER, 2);
                }
                userInfo.putSFSObject(Keys.GAME, gameInfo);
                return userInfo;
            }
        } catch (Exception e) {
            log.error("getUserInfo ", e);
        }
        return null;
    }

    @Override
    public ISFSObject getListUserInfoForJoinRoom(List<User> list, String roomName) {
        if (list != null) {
            ISFSObject result = SFSObject.newInstance();
            for (int i = 0; i < list.size(); i++) {
                User user = list.get(i);
                long userId = getUserId(user.getName());
                if (userId > 0) {
                    ISFSObject obj = getUserInfoForJoinRoom(userId, roomName);
                    if (obj != null) {
                        result.putSFSObject(user.getName(), obj);
                    }
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public List<UserVariable> getUserVariableForJoinRoom(String username, String roomName) { // when user join room, notify for all player in this room by getApi().setUserVariables
        List<UserVariable> variableList = new ArrayList<UserVariable>();
        BeecasUser user = findUser(username);
        if (user != null) {
            UserVariable userVariable = new SFSUserVariable(Keys.USERNAME, user.getUsername());
            variableList.add(userVariable);
            userVariable = new SFSUserVariable(Keys.FULL_NAME, user.getFullname());
            variableList.add(userVariable);
            ISFSObject avatarObj = SFSObject.newInstance();
            List<Long> frames = new ArrayList<Long>();
            avatarObj.putLongArray(Keys.AVATAR_FRAMES, frames);
            List<Short> sequences = new ArrayList<Short>();
            avatarObj.putShortArray(Keys.AVATAR_SEQUENCES, sequences);
            userVariable = new SFSUserVariable(Keys.AVATAR, avatarObj);
            variableList.add(userVariable);
            userVariable = new SFSUserVariable(Keys.GENDER, user.getGender());
            variableList.add(userVariable);
            userVariable = new SFSUserVariable(Keys.STATUS, 1);
            variableList.add(userVariable);
            userVariable = new SFSUserVariable(Keys.VIP, user.getVip());
            variableList.add(userVariable);
            ISFSObject gameInfo = SFSObject.newInstance();
            if (roomName.indexOf("GM") >= 0) {
                gameInfo.putInt(Keys.GAME_TYPE, Common.GAME_GOMOKU);
                gameInfo.putUtfString(Keys.GAME_NAME, "Gomoku");
                gameInfo.putShort(Keys.GAME_LEVEL, (short) 11);
                gameInfo.putInt(Keys.GAME_SCORE, 1);
                gameInfo.putInt(Keys.GAME_TABLES_PLAYED_NUMBER, 1);
                gameInfo.putInt(Keys.GAME_TABLES_WON_NUMBER, 1);
                gameInfo.putInt(Keys.GAME_TABLES_LOST_NUMBER, 1);
            } else if (roomName.indexOf("BT") > 0) {
                gameInfo.putInt(Keys.GAME_TYPE, Common.GAME_BIG_TWO);
                gameInfo.putUtfString(Keys.GAME_NAME, "BigTwo");
                gameInfo.putShort(Keys.GAME_LEVEL, (short) 22);
                gameInfo.putInt(Keys.GAME_SCORE, 2);
                gameInfo.putInt(Keys.GAME_TABLES_PLAYED_NUMBER, 2);
                gameInfo.putInt(Keys.GAME_TABLES_WON_NUMBER, 2);
                gameInfo.putInt(Keys.GAME_TABLES_LOST_NUMBER, 2);
            }
            userVariable = new SFSUserVariable(Keys.GAME, gameInfo);
            variableList.add(userVariable);
        }
        return variableList;
    }

    @Override
    public List<UserVariable> getUserVariableForLogin(String username) {
        List<UserVariable> variableList = new ArrayList<UserVariable>();
        BeecasUser user = findUser(username);
        if (user != null) {
            UserVariable userVariable = new SFSUserVariable(Keys.USERNAME, user.getUsername());
            variableList.add(userVariable);
            userVariable = new SFSUserVariable(Keys.FULL_NAME, user.getFullname());
            variableList.add(userVariable);
            ISFSObject avatarObj = SFSObject.newInstance();
            List<Long> frames = new ArrayList<Long>();
            avatarObj.putLongArray(Keys.AVATAR_FRAMES, frames);
            List<Short> sequences = new ArrayList<Short>();
            avatarObj.putShortArray(Keys.AVATAR_SEQUENCES, sequences);
            userVariable = new SFSUserVariable(Keys.AVATAR, avatarObj);
            userVariable = new SFSUserVariable(Keys.GENDER, user.getGender());
            variableList.add(userVariable);
            userVariable = new SFSUserVariable(Keys.STATUS, 1);
            variableList.add(userVariable);
            userVariable = new SFSUserVariable(Keys.MONEY, user.getMoney());
            variableList.add(userVariable);
            userVariable = new SFSUserVariable(Keys.VIP, user.getVip());
            variableList.add(userVariable);
        }
        return variableList;
    }

    private boolean validateUsername(String source) {
        int length = source.length();
        if (length < 6 && length > 60) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char a = source.charAt(i);
            if (!Character.isLetterOrDigit(a) || !Character.isLowerCase(a)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ISFSObject getBuddyInfo(long userId) {
        try {
            BeecasUser beecasUser = findUser(userId);
            if (beecasUser != null) {
                ISFSObject buddyInfo = SFSObject.newInstance();
                buddyInfo.putUtfString(Keys.USERNAME, beecasUser.getUsername());
                buddyInfo.putUtfString(Keys.FULL_NAME, beecasUser.getFullname());
                buddyInfo.putByte(Keys.GENDER, (byte) beecasUser.getGender());
                ISFSObject avatarObj = SFSObject.newInstance();
                List<Long> frames = new ArrayList<Long>();
                avatarObj.putLongArray(Keys.AVATAR_FRAMES, frames);
                List<Short> sequences = new ArrayList<Short>();
                avatarObj.putShortArray(Keys.AVATAR_SEQUENCES, sequences);
                buddyInfo.putSFSObject(Keys.AVATAR, avatarObj);
                buddyInfo.putByte(Keys.STATUS, (byte) Show.Online.value()); //Get from subcriberservice
                buddyInfo.putUtfString(Keys.STATUS_STRING, "On BEECAS"); //Get from subcriberservice
                buddyInfo.putByte(Keys.VIP, (byte) beecasUser.getVip());
                buddyInfo.putUtfString(Keys.VIP_DETAIL, "Vip Gold");
                buddyInfo.putShort(Keys.LEVEL, (short) beecasUser.getLevel());
                ISFSArray symbolsLevel = SFSArray.newInstance();
                for (int i = 0; i < 3; i++) {
                    symbolsLevel.addByte((byte) 1);
                }
                buddyInfo.putSFSArray(Keys.LEVEL_SYMBOLS, symbolsLevel);
                return buddyInfo;
            }
        } catch (Exception e) {
            log.error("getBuddyInfo ", e);
        }
        return null;
    }

    @Override
    public ISFSObject getBuddyInfo(String username) {
        long userId = getUserId(username);
        if (userId > 0) {
            return getBuddyInfo(userId);
        }
        return null;
    }

    @Override
    public ISFSObject getUserVariableForProfile(String username, boolean isOwner) {
        //        List<UserVariable> variableList = new ArrayList<UserVariable>();
        //        BeecasUser user = findUser(username);
        //        if (user != null) {
        //            UserVariable userVariable = new SFSUserVariable(Keys.USERNAME, user.getUsername());
        //            variableList.add(userVariable);
        //            userVariable = new SFSUserVariable(Keys.FULL_NAME, user.getFullname());
        //            variableList.add(userVariable);
        ////            if (user.getEmailIsPrivate() == Common.HIDE_EMAIL) {
        ////                String email = user.getEmail();
        ////                if (email == null) {
        ////                    email = "";
        ////                }
        ////                userVariable = new SFSUserVariable(Keys.EMAIL, email);
        ////                variableList.add(userVariable);
        ////            }
        //            ISFSObject avatarObj = SFSObject.newInstance();
        //            List<Long> frames = new ArrayList<Long>();
        //            avatarObj.putLongArray(Keys.AVATAR_FRAMES, frames);
        //            List<Short> sequences = new ArrayList<Short>();
        //            avatarObj.putShortArray(Keys.AVATAR_SEQUENCES, sequences);            
        //            userVariable = new SFSUserVariable(Keys.AVATAR, avatarObj);
        //            userVariable = new SFSUserVariable(Keys.GENDER, user.getGender());
        //            variableList.add(userVariable);
        //            if (user.getBirthdayIsPrivate() == Common.SHOW_FULL_BIRTHDAY) {
        //                userVariable = new SFSUserVariable(Keys.BIRTHDAY, Utility.convertDateTimeBirthdayToString(user.getBirthday()));
        //                variableList.add(userVariable);
        //            } else if (user.getBirthdayIsPrivate() == Common.SHOW_ONLY_MONTH_DAY_BIRTHDAY) {
        //                userVariable = new SFSUserVariable(Keys.BIRTHDAY, Utility.convertDateTimeBirthdayOnlyDateToString(user.getBirthday()));
        //                variableList.add(userVariable);
        //            } else {
        //                userVariable = new SFSUserVariable(Keys.BIRTHDAY, "");
        //                variableList.add(userVariable);
        //            }
        //            if (user.getCountryIsPrivate() == Common.HIDE_COUNTRY) {
        //                userVariable = new SFSUserVariable(Keys.COUNTRY, user.getCountry());
        //                variableList.add(userVariable);
        //            }
        //            userVariable = new SFSUserVariable(Keys.UPDATED_TIME, Utility.convertDateTimeUpdateTimeToString(user.getUpdateTime()));
        //            variableList.add(userVariable);
        //            userVariable = new SFSUserVariable(Keys.STATUS, 1);
        //            variableList.add(userVariable);
        //            userVariable = new SFSUserVariable(Keys.STATUS_STRING, "On BEECAS");
        //            variableList.add(userVariable);
        //            userVariable = new SFSUserVariable(Keys.MONEY, user.getMoney());
        //            variableList.add(userVariable);
        //            userVariable = new SFSUserVariable(Keys.VIP, user.getVip());
        //            variableList.add(userVariable);
        //            userVariable = new SFSUserVariable(Keys.VIP_DETAIL, "Vip Diamond");
        //            variableList.add(userVariable);
        //            userVariable = new SFSUserVariable(Keys.LEVEL, user.getLevel());
        //            variableList.add(userVariable);
        //            ISFSArray symbolsLevel = SFSArray.newInstance();
        //            for (int i = 0; i < 3; i++) {
        //                symbolsLevel.addByte((byte) 1);
        //            }
        //            userVariable = new SFSUserVariable(Keys.LEVEL_SYMBOLS, symbolsLevel);
        //            variableList.add(userVariable);
        //
        //            ISFSObject gameObject = SFSObject.newInstance();
        //            gameObject.putInt(Keys.GAME_TYPE, 1);
        //            gameObject.putUtfString(Keys.GAME_NAME, "");
        //            gameObject.putShort(Keys.GAME_LEVEL, (short) 1);
        //            gameObject.putInt(Keys.GAME_SCORE, 1000000000);
        //            gameObject.putInt(Keys.GAME_TABLES_LOST_NUMBER, 1);
        //            gameObject.putInt(Keys.GAME_TABLES_PLAYED_NUMBER, 2);
        //            gameObject.putInt(Keys.GAME_TABLES_WON_NUMBER, 3);
        //            userVariable = new SFSUserVariable(Keys.GAME, gameObject);
        //            variableList.add(userVariable);
        //        }
        //        return variableList;

        try {
            BeecasUser beecasUser = findUser(username);
            if (beecasUser != null) {
                ISFSObject userInfo = SFSObject.newInstance();
                userInfo.putUtfString(Keys.USERNAME, beecasUser.getUsername());
                userInfo.putUtfString(Keys.FULL_NAME, beecasUser.getFullname());
                userInfo.putByte(Keys.GENDER, (byte) beecasUser.getGender());
                ISFSObject avatarObj = SFSObject.newInstance();
                List<Long> frames = new ArrayList<Long>();
                avatarObj.putLongArray(Keys.AVATAR_FRAMES, frames);
                List<Short> sequences = new ArrayList<Short>();
                avatarObj.putShortArray(Keys.AVATAR_SEQUENCES, sequences);
                userInfo.putSFSObject(Keys.AVATAR, avatarObj);
                if (!isOwner) {
                    if (beecasUser.getBirthdayIsPrivate() == Common.SHOW_FULL_BIRTHDAY) {
                        userInfo.putUtfString(Keys.BIRTHDAY, Utility.convertDateTimeBirthdayToString(beecasUser.getBirthday()));
                    } else if (beecasUser.getBirthdayIsPrivate() == Common.SHOW_ONLY_MONTH_DAY_BIRTHDAY) {
                        userInfo.putUtfString(Keys.BIRTHDAY, Utility.convertDateTimeBirthdayOnlyDateToString(beecasUser.getBirthday()));
                    } else {
                        userInfo.putUtfString(Keys.BIRTHDAY, "");
                    }
                } else {
                    userInfo.putUtfString(Keys.BIRTHDAY, Utility.convertDateTimeBirthdayToString(beecasUser.getBirthday()));
                }
                if (beecasUser.getCountryIsPrivate() != Common.HIDE_COUNTRY && !isOwner) {
                    userInfo.putUtfString(Keys.COUNTRY, beecasUser.getCountry());
                } else {
                    userInfo.putUtfString(Keys.COUNTRY, beecasUser.getCountry());
                }
                userInfo.putByte(Keys.STATUS, (byte) Show.Online.value()); //Get from subcriberservice
                userInfo.putUtfString(Keys.STATUS_STRING, "On BEECAS"); //Get from subcriberservice
                if (isOwner) {
                    userInfo.putLong(Keys.MONEY, beecasUser.getMoney());
                }
                userInfo.putByte(Keys.VIP, (byte) beecasUser.getVip());
                userInfo.putUtfString(Keys.VIP_DETAIL, "Vip Gold");
                userInfo.putShort(Keys.LEVEL, (short) beecasUser.getLevel());
                ISFSArray symbolsLevel = SFSArray.newInstance();
                for (int i = 0; i < 3; i++) {
                    symbolsLevel.addByte((byte) 1);
                }
                userInfo.putSFSArray(Keys.LEVEL_SYMBOLS, symbolsLevel);
                ISFSArray tableJoining = SFSArray.newInstance();
                ISFSObject tableObj = SFSObject.newInstance();
                List<String> roomNameArray = new ArrayList<String>();
                tableObj.putUtfStringArray(Keys.GAME_ROOM_NAMES_ARRAY, roomNameArray);
                List<String> roomNameDisplayArray = new ArrayList<String>();
                tableObj.putUtfStringArray(Keys.GAME_ROOM_DISPLAY_NAMES_ARRAY, roomNameDisplayArray);
                tableObj.putUtfString(Keys.ROOM_GROUP_DISPLAY_NAME, "");
                tableObj.putUtfString(Keys.ROOM_DISPLAY_NAME, "");
                tableObj.putUtfString(Keys.GAME_NAME, "");
                tableJoining.addSFSObject(tableObj);
                userInfo.putSFSArray(Keys.TABLES_JOINING, tableJoining);
                return userInfo;
            }
        } catch (Exception e) {
            log.error("getBuddyInfo ", e);
        }
        return null;
    }
}
