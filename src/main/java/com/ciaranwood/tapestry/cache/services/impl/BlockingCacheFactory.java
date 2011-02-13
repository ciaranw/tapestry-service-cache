package com.ciaranwood.tapestry.cache.services.impl;

import com.ciaranwood.tapestry.cache.services.CacheFactory;
import com.ciaranwood.tapestry.cache.services.ServiceCacheConstants;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.blocking.BlockingCache;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

public class BlockingCacheFactory implements CacheFactory {

    private final CacheManager cacheManager;
    private final int timeout;

    public BlockingCacheFactory(@Inject @Symbol(ServiceCacheConstants.EHCACHE_CONFIGURATION_FILE) String configFileUrl,
                                @Inject @Symbol(ServiceCacheConstants.BLOCKING_CACHE_TIMEOUT) int timeout) {
        this.cacheManager = new CacheManager(getClass().getResource(configFileUrl));
        this.timeout = timeout;
    }

    public Ehcache getCache(String cacheName) {
        if(!cacheManager.cacheExists(cacheName)) {
           cacheManager.addCache(cacheName);
        }

        Ehcache cache = cacheManager.getEhcache(cacheName);
        BlockingCache blockingCache = new BlockingCache(cache);
        blockingCache.setTimeoutMillis(timeout);
        return blockingCache;
    }
}
