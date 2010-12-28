package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheKey;
import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import com.ciaranwood.tapestry.cache.services.annotations.WriteThrough;

public class StringStoreDao implements StringDao {

    private final StringStore store;

    public StringStoreDao(StringStore store) {
        this.store = store;
    }

    @CacheResult
    public String get(@CacheKey Integer id) {
        return store.get(id);
    }

    @WriteThrough
    public void put(@CacheKey Integer id, String data) {
        store.put(id, data);
    }
}
