package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;

public class AnotherTestServiceImpl implements AnotherTestService {

    public String interfaceMethod() {
        return null;
    }

    @CacheResult
    public String nonInterfaceMethod() {
        return null;
    }
}
