package com.beecas.service;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beecas.constants.Keys;
import com.beecas.constants.UserConstants;
import com.google.inject.Inject;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.smartfoxserver.v2.SmartFoxServer;

public class DatabaseService implements IDatabaseService {
    private static Logger log = LoggerFactory.getLogger(UserConstants.BEECAS_LOG);

    private List<String> activeShard;

    private TIntObjectHashMap<String> shards;

    private BoneCP shardPool;

    private BoneCP beecasPool1;

    private BoneCP beecasPool2;

    private BoneCP commonPool;

    private List<BoneCP> beecasPoolList;

    @Inject
    private DatabaseService() {
        activeShard = new ArrayList<String>();
        shards = new TIntObjectHashMap<String>();
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            String curDir = System.getProperty("user.dir");
            String fileSeparator = System.getProperty("file.separator");
            String path = curDir + fileSeparator;
            log.info("classPath " + path);
            try {
                Properties properties = SmartFoxServer.getInstance().getZoneManager().getZoneByName(UserConstants.TURN_BASED_ZONE).getExtension().getConfigProperties();
                String host = properties.getProperty("shard_host");
                String user = properties.getProperty("shard_user");
                String password = properties.getProperty("shard_password");
                String database = properties.getProperty("shard_database");
                BoneCPConfig config = new BoneCPConfig();
                config.setJdbcUrl("jdbc:mysql://" + host + "/" + database + "?useUnicode=yes&characterEncoding=UTF-8"); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
                config.setUsername(user);
                config.setPassword(password);
                config.setMinConnectionsPerPartition(5);
                config.setMaxConnectionsPerPartition(10);
                config.setPartitionCount(1);
                shardPool = new BoneCP(config); // setup the connection pool
                Connection connection = shardPool.getConnection(); // fetch a connection
                if (connection != null) {
                    log.info("Get Shard connection successfully");
                    connection.close();
                }
                host = properties.getProperty("beecas1_host");
                user = properties.getProperty("beecas1_user");
                password = properties.getProperty("beecas1_password");
                database = properties.getProperty("beecas1_database");
                config = new BoneCPConfig();
                config.setJdbcUrl("jdbc:mysql://" + host + "/" + database + "?useUnicode=yes&characterEncoding=UTF-8"); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
                config.setUsername(user);
                config.setPassword(password);
                config.setMinConnectionsPerPartition(5);
                config.setMaxConnectionsPerPartition(10);
                config.setPartitionCount(1);
                beecasPool1 = new BoneCP(config); // setup the connection pool
                connection = beecasPool1.getConnection(); // fetch a connection
                if (connection != null) {
                    log.info("Get Beecas1 connection successfully");
                    connection.close();
                }

                host = properties.getProperty("beecas2_host");
                user = properties.getProperty("beecas2_user");
                password = properties.getProperty("beecas2_password");
                database = properties.getProperty("beecas2_database");
                config = new BoneCPConfig();
                config.setJdbcUrl("jdbc:mysql://" + host + "/" + database + "?useUnicode=yes&characterEncoding=UTF-8"); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
                config.setUsername(user);
                config.setPassword(password);
                config.setMinConnectionsPerPartition(5);
                config.setMaxConnectionsPerPartition(10);
                config.setPartitionCount(1);
                beecasPool2 = new BoneCP(config); // setup the connection pool
                connection = beecasPool2.getConnection(); // fetch a connection
                if (connection != null) {
                    log.info("Get Beecas2 connection successfully");
                    connection.close();
                }

                host = properties.getProperty("common_host");
                user = properties.getProperty("common_user");
                password = properties.getProperty("common_password");
                database = properties.getProperty("common_database");
                config = new BoneCPConfig();
                config.setJdbcUrl("jdbc:mysql://" + host + "/" + database + "?useUnicode=yes&characterEncoding=UTF-8"); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
                config.setUsername(user);
                config.setPassword(password);
                config.setMinConnectionsPerPartition(5);
                config.setMaxConnectionsPerPartition(10);
                config.setPartitionCount(1);
                commonPool = new BoneCP(config); // setup the connection pool
                connection = commonPool.getConnection(); // fetch a connection
                if (connection != null) {
                    log.info("Get Common connection successfully");
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        beecasPoolList = new ArrayList<BoneCP>();
        beecasPoolList.add(beecasPool1);
        beecasPoolList.add(beecasPool2);
        GetShards();
    }

    @Override
    public Connection getSFSConnection() {
        try {
            return SmartFoxServer.getInstance().getZoneManager().getZoneByName(UserConstants.TURN_BASED_ZONE).getDBManager().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void GetShards() {
        try {
            Connection connection = null;
            PreparedStatement stm = null;
            try {
                connection = shardPool.getConnection();
                stm = connection.prepareStatement("select * from " + Keys.SHARD_TB);
                ResultSet result = stm.executeQuery();
                while (result.next()) {
                    int shardId = result.getInt(Keys.SHARD_ID_DB);
                    String dataSource = result.getString(Keys.CONNECTION_STRING_DB);
                    int status = result.getInt(Keys.STATUS_SHARD_DB);
                    result.getTimestamp(Keys.CREATE_DATE_SHARDB).getTime();
                    shards.put(shardId, dataSource);
                    if (status > 0) {
                        activeShard.add(dataSource);
                    }
                }
            } finally {
                if (connection != null) {
                    stm.close();
                    connection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnectionByUserId(long userId) {
        try {
            int shardId = lookupShardByUserId(userId);
            if (shardId > 0) {
                return beecasPoolList.get(shardId - 1).getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Connection getConnectionByUsername(String username) {
        try {
            int shardId = lookupShardByUsername(username);
            if (shardId > 0) {
                return beecasPoolList.get(shardId - 1).getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Connection getActiveConnection(int[] shardId) {
        int index = (int) (System.currentTimeMillis() % activeShard.size());
        try {
            shardId[0] = index + 1;
            return beecasPoolList.get(index).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Connection getCommonConnection() {
        try {
            return commonPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int lookupShardByUserId(long userId) {
        try {
            Connection connection = null;
            PreparedStatement stm = null;
            try {
                connection = shardPool.getConnection();
                stm = connection.prepareStatement("select " + Keys.SHARD_ID_LOOKUP_DB + " from " + Keys.USER_LOOKUP_TB + " where " + Keys.USER_ID_LOOKUP_DB + " = '" + userId + "'");
                ResultSet result = stm.executeQuery();
                while (result.next()) {
                    return result.getInt(Keys.SHARD_ID_LOOKUP_DB);
                }
            } finally {
                if (connection != null) {
                    stm.close();
                    connection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int lookupShardByUsername(String username) {
        try {
            Connection connection = null;
            PreparedStatement stm = null;
            try {
                connection = shardPool.getConnection();
                stm = connection.prepareStatement("select " + Keys.SHARD_ID_LOOKUP_DB + " from " + Keys.USER_LOOKUP_TB + " where " + Keys.USERNAME_LOOKUP_DB + " = '" + username + "'");
                ResultSet result = stm.executeQuery();
                while (result.next()) {
                    return result.getInt(Keys.SHARD_ID_LOOKUP_DB);
                }
            } finally {
                if (connection != null) {
                    stm.close();
                    connection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
