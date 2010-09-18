package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;

public class InvalidCacheAnnotationsImpl implements InvalidCacheAnnotations {

    @CacheResult
    public String cantCache(String argument1, String argument2) {
        return null;
    }
}
