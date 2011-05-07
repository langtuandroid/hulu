package com.beecas.service;

import java.sql.Connection;

public interface IDatabaseService {
    Connection getSFSConnection();

    Connection getConnectionByUserId(long userId);

    Connection getConnectionByUsername(String username);

    Connection getActiveConnection(int[] shardId);

    Connection getCommonConnection();
}
