package com.ciaranwood.tapestry.cache.services;

public interface StringDao {

    String get(Integer id);

    void put(Integer id, String data);

}
