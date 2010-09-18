package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheKey;
import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;

import java.util.HashMap;
import java.util.Map;

public class TestServiceImpl implements TestService {

    private int counter1 = 0;
    private int counter2 = 0;
    private Map<String, Integer> counterUsingKey = new HashMap<String, Integer>();

    @CacheResult
    public String cacheThis() {
        return "counter: " + counter1++;
    }

    public String dontCacheThis() {
        return "counter: " + counter2++;
    }

    @CacheResult
    public String cacheThisUsingKey(@CacheKey String key) {
        if(!counterUsingKey.containsKey(key)) {
            counterUsingKey.put(key, 0);
        }

        Integer counter = counterUsingKey.get(key);
        counterUsingKey.put(key, counter + 1);
        return "cached using key " + key + ", counter: " + counter;
    }
}
