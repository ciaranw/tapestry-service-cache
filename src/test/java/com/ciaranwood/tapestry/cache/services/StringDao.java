package com.ciaranwood.tapestry.cache.services;

public interface StringDao {

    String get(Integer key);

    void put(Integer key, String data);

}
