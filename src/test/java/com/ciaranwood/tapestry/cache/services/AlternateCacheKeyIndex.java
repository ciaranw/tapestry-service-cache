package com.ciaranwood.tapestry.cache.services;

public interface AlternateCacheKeyIndex {

    Integer get(Integer key);

    void put(Integer value, Integer key);

}
