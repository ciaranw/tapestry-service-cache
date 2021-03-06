package com.ciaranwood.tapestry.cache.services;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class StringStore {

    private Map<Integer, String> store = new HashMap<Integer, String>();
    private final Logger log;

    public StringStore(Logger log) {
        this.log = log;
    }

    public String get(Integer id) {
        return store.get(id);
    }

    public void put(Integer id, String data) {
        log.debug("putting {} using id {}", data, id);
        store.put(id, data);
    }

}
