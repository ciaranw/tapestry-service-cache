package com.ciaranwood.tapestry.cache.services;

import net.sf.ehcache.Ehcache;

public interface CacheFactory {

    Ehcache getCache(String cacheName);
    
}
