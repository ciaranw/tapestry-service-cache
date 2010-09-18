package com.ciaranwood.tapestry.cache.services;

public interface TestService {

    String cacheThis();

    String dontCacheThis();

    String cacheThisUsingKey(String key);

}
