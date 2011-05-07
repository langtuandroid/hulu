package com.beecas.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import com.beecas.constants.UserConstants;
import com.google.inject.Inject;
import com.smartfoxserver.v2.SmartFoxServer;

public class CacheService implements ICacheService {

    private List<MemcachedClient> caches;

    private AtomicInteger index;

    private int cachePoolSize;

    @Inject
    private CacheService() throws IOException {
        Properties properties = SmartFoxServer.getInstance().getZoneManager().getZoneByName(UserConstants.TURN_BASED_ZONE).getExtension().getConfigProperties();
        index = new AtomicInteger(0);
        caches = new ArrayList<MemcachedClient>();
        cachePoolSize = Integer.parseInt(properties.getProperty("cachePoolSize", "10"));
        for (int i = 0; i < cachePoolSize; i++) {
            caches.add(new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(properties.getProperty("memcachedServers"))));
        }
    }

    @Override
    public MemcachedClient getCache() {
        System.out.println("GetCache.................");
        int curIndex = index.getAndIncrement() % cachePoolSize;
        return caches.get(curIndex);
        //        return null;
    }
}
