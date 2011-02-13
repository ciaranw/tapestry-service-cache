package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheKey;
import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import com.ciaranwood.tapestry.cache.services.annotations.WriteThrough;

public class AlternateCacheKeyIndexImpl implements AlternateCacheKeyIndex {

    private final IntegerStore store;

    public AlternateCacheKeyIndexImpl(IntegerStore store) {
        this.store = store;
    }

    @CacheResult
    public Integer get(@CacheKey Integer key) {
        return store.get(key);
    }

    @WriteThrough
    public void put(Integer value, @CacheKey Integer key) {
        store.put(key, value);
    }
}
