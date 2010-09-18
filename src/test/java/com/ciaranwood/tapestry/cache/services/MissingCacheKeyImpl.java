package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;

public class MissingCacheKeyImpl implements MissingCacheKey {

    @CacheResult
    public String missingCacheKey(String key) {
        return null;
    }
}
