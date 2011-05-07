package com.beecas.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jredis.JRedis;
import org.jredis.RedisException;
import org.jredis.ri.alphazero.JRedisClient;

import com.beecas.constants.Common;
import com.beecas.constants.UserConstants;
import com.google.inject.Inject;
import com.smartfoxserver.v2.SmartFoxServer;

public class SubscriberService implements ISubscriberService {
    private static final String SUBSCRIBER_PREFIX = "Subs:";

    private List<JRedis> jredisList;

    @Inject
    private SubscriberService() {
        Properties properties = SmartFoxServer.getInstance().getZoneManager().getZoneByName(UserConstants.TURN_BASED_ZONE).getExtension().getConfigProperties();
        jredisList = new ArrayList<JRedis>();
        int n = Integer.parseInt(properties.getProperty("subscriberRedisCount"));
        for (int i = 0; i < n; i++) {
            String[] hostInfos = properties.getProperty("subscriberRedis" + i).split(":");
            int port = Integer.parseInt(hostInfos[1]);
            jredisList.add(new JRedisClient(hostInfos[0], port));
        }
    }

    @Override
    public List<String> getSubscribers(String id) {
        JRedis redis = getRedis(id);
        try {
            List<byte[]> subscribersBytes = redis.smembers(SUBSCRIBER_PREFIX + id);
            if (subscribersBytes != null) {
                int size = subscribersBytes.size();
                List<String> subscribers = new ArrayList<String>(size);
                for (int i = 0; i < size; i++) {
                    try {
                        subscribers.add(new String(subscribersBytes.get(i), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return subscribers;
            }
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void subscribe(String id, List<String> subscribers) {
        JRedis redis = getRedis(id);
        int size = subscribers.size();
        String key = SUBSCRIBER_PREFIX + id;
        for (int i = 0; i < size; i++) {
            try {
                redis.sadd(key, subscribers.get(i));
            } catch (RedisException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void subscribe(String id, String subscriber) {
        JRedis redis = getRedis(id);
        try {
            redis.sadd(SUBSCRIBER_PREFIX + id, subscriber);
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unSubscribe(String id, String subscriber) {
        JRedis redis = getRedis(id);
        try {
            redis.srem(SUBSCRIBER_PREFIX + id, subscriber);
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    private JRedis getRedis(String id) {
        int index = Math.abs(id.hashCode()) % jredisList.size();
        return jredisList.get(index);
    }
}
