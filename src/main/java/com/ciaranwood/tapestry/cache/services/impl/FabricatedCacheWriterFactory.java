package com.ciaranwood.tapestry.cache.services.impl;

import com.ciaranwood.tapestry.cache.services.CacheWriterClassFactory;
import com.ciaranwood.tapestry.cache.services.CacheWriterClassFactoryLocator;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.CacheWriterFactory;

import java.util.Properties;

public class FabricatedCacheWriterFactory extends CacheWriterFactory {

    private CacheWriterClassFactory fabricatedClassFactory;

    public FabricatedCacheWriterFactory() {
        fabricatedClassFactory = CacheWriterClassFactoryLocator.getClassFactory();
    }

    @Override
    public CacheWriter createCacheWriter(Ehcache cache, Properties properties) {
        return fabricatedClassFactory.getCacheWriterForCache(cache);
    }
}
