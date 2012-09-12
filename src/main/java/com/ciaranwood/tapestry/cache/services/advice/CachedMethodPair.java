package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import com.ciaranwood.tapestry.cache.services.annotations.WriteThrough;

import java.lang.reflect.Method;

public class CachedMethodPair {

    private final String methodKey;
    private CacheResult readAnnotation;
    private Method readMethod;
    private WriteThrough writeAnnotation;
    private Method writeMethod;

    public CachedMethodPair(String methodKey) {
        this.methodKey = methodKey;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public void setRead(Method readMethod, CacheResult readAnnotation) {
        this.readMethod = readMethod;
        this.readAnnotation = readAnnotation;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public void setWrite(Method writeMethod, WriteThrough writeAnnotation) {
        this.writeMethod = writeMethod;
        this.writeAnnotation = writeAnnotation;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public String getCacheName() {
        if(readMethod == null && writeMethod == null) {
            throw new RuntimeException("Cannot determine cache name: neither read or write methods found!");
        }

        if(readAnnotation == null) {
            return writeAnnotation.cacheName();
        } else {
            return readAnnotation.cacheName();
        }
    }

}
