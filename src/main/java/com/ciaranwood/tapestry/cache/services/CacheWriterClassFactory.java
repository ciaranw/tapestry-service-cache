package com.ciaranwood.tapestry.cache.services;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;

import java.lang.reflect.Method;

public interface CacheWriterClassFactory {
    
    <T> void add(Ehcache cache, Class<T> serviceInterface, T instance, Method write);

    CacheWriter getCacheWriterForCache(Ehcache cache);
}
