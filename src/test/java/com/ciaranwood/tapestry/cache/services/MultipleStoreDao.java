package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheKey;
import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import com.ciaranwood.tapestry.cache.services.annotations.WriteThrough;

public class MultipleStoreDao implements MultipleDao {

    private final StringStore stringStore;
    private final IntegerStore integerStore;

    public MultipleStoreDao(StringStore stringStore, IntegerStore integerStore) {
        this.stringStore = stringStore;
        this.integerStore = integerStore;
    }

    @CacheResult("StringStore")
    public String getString(@CacheKey Integer id) {
        return stringStore.get(id);
    }

    @WriteThrough("StringStore")
    public void putString(@CacheKey Integer id, String data) {
        stringStore.put(id, data);
    }

    @CacheResult("IntegerStore")
    public Integer getInteger(@CacheKey Integer id) {
        return integerStore.get(id);
    }

    @WriteThrough("IntegerStore")
    public void putInteger(@CacheKey Integer id, Integer data) {
        integerStore.put(id, data);
    }
}
