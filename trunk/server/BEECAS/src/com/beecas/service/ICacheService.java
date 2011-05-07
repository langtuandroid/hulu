package com.beecas.service;

import net.spy.memcached.MemcachedClient;

public interface ICacheService {

    MemcachedClient getCache();

}
