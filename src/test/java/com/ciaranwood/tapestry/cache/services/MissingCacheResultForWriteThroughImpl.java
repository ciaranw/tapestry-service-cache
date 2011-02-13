package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheKey;
import com.ciaranwood.tapestry.cache.services.annotations.WriteThrough;

public class MissingCacheResultForWriteThroughImpl implements MissingCacheResultForWriteThrough {

    public String get(Integer id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @WriteThrough
    public void put(@CacheKey Integer id, String data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
