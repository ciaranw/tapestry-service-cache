package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import com.ciaranwood.tapestry.cache.services.annotations.WriteThrough;

import java.lang.reflect.Method;

public class CachedMethodPair {

    private final String methodKey;
    private Method read;
    private Method write;

    public CachedMethodPair(String methodKey) {
        this.methodKey = methodKey;
    }

    public Method getRead() {
        return read;
    }

    public Method getWrite() {
        return write;
    }

    public void setRead(Method read) {
        this.read = read;
    }

    public void setWrite(Method write) {
        this.write = write;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public String getCacheName() {
        if(read == null && write == null) {
            throw new RuntimeException("Cannot determine cache name: neither read or write methods found!");
        }

        if(read == null) {
            WriteThrough writeThrough = write.getAnnotation(WriteThrough.class);
            return writeThrough.cacheName();
        } else {
            CacheResult cacheResult = read.getAnnotation(CacheResult.class);
            return cacheResult.cacheName();
        }
    }

}
