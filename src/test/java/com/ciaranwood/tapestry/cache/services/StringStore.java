package com.ciaranwood.tapestry.cache.services;

import java.util.HashMap;
import java.util.Map;

public class StringStore {

    private Map<Integer, String> store = new HashMap<Integer, String>();

    public String get(Integer id) {
        return store.get(id);
    }

    public void put(Integer id, String data) {
        store.put(id, data);
    }

}
