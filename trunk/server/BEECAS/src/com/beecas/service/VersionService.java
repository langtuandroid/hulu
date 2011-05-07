package com.beecas.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beecas.constants.Common;
import com.beecas.constants.UserConstants;
import com.google.inject.Inject;
import com.smartfoxserver.v2.SmartFoxServer;

public class VersionService implements IVersionService {
    private IDatabaseService databaseService;

    private String currentVerion;

    private ScheduledFuture<?> taskHandle;

    private static Logger log = LoggerFactory.getLogger(UserConstants.BEECAS_LOG);

    @Inject
    private VersionService(IDatabaseService databaseService) {
        this.databaseService = databaseService;
        taskHandle = SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new UpdateVersionTask(), 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public String getCurrentVersion() {
        return currentVerion;
    }

    private class UpdateVersionTask implements Runnable {

        private Logger log1 = LoggerFactory.getLogger(UserConstants.BEECAS_LOG);

        public void run() {
            Connection connection = null;
            Statement stm = null;
            try {
                try {
                    connection = databaseService.getSFSConnection();
                    stm = connection.createStatement();
                    ResultSet result = stm.executeQuery("select * from client_version");
                    log1.info("Timer get version running... ");
                    while (result.next()) {
                        currentVerion = result.getString("version");
                    }
                } finally {
                    if (connection != null) {
                        stm.close();
                        connection.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
