package com.ciaranwood.tapestry.cache.services;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class IntegerStore {

    private Map<Integer, Integer> store = new HashMap<Integer, Integer>();
    private final Logger log;

    public IntegerStore(Logger log) {
        this.log = log;
    }

    public Integer get(Integer id) {
        return store.get(id);
    }

    public void put(Integer id, Integer data) {
        log.debug("putting {} using id {}", data, id);
        store.put(id, data);
    }

}
