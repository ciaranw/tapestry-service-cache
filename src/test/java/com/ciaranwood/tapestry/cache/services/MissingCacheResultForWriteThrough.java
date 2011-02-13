package com.ciaranwood.tapestry.cache.services;

public interface MissingCacheResultForWriteThrough {

    String get(Integer id);

    void put(Integer id, String data);

}
