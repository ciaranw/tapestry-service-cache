package com.ciaranwood.tapestry.cache.services;

public interface MultipleDao {

    String getString(Integer id);

    void putString(Integer id, String data);

    Integer getInteger(Integer id);

    void putInteger(Integer id, Integer data);

}
