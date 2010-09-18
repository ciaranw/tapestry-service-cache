package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;

public class NoInterface {

    private int counter = 0;

    @CacheResult
    public String cannotCacheThis() {
       return "counter: " + counter++; 
    }

}
