package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.annotations.CacheKey;
import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import com.ciaranwood.tapestry.cache.services.impl.SwitchingCacheWriter;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.plastic.MethodAdvice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CacheAdviceBuilder<T> {

    private final T delegate;
    private final ServiceResources resources;
    private final Ehcache ehcache;

    public CacheAdviceBuilder(T delegate, ServiceResources resources, Ehcache ehcache) {
        this.delegate = delegate;
        this.resources = resources;
        this.ehcache = ehcache;
    }

    public MethodAdvice buildReadMethodAdvice(String methodKey, Method method) {
        int cacheKeyIndex = getCacheKeyParameterIndex(method);

        checkForIncorrectParameterCount(method);
        validateCacheKeyAnnotation(method, cacheKeyIndex);

        return new CacheMethodAdvice(ehcache, cacheKeyIndex, methodKey, resources.getLogger());
    }

    public MethodAdvice buildWriteMethodAdvice(String methodKey, Method method) {
        int cacheKeyIndex = getCacheKeyParameterIndex(method);
        return new WriteThroughAdvice(ehcache, methodKey, cacheKeyIndex, resources.getLogger());
    }

    private void checkForIncorrectParameterCount(Method method) {
        if (method.getParameterTypes().length > 1) {
            throw new RuntimeException(String.format("Cannot apply caching to %s, cached methods should have no more than one parameter.",
                    method));
        }
    }

    private void validateCacheKeyAnnotation(Method method, int cacheKeyIndex) {
        if (cacheKeyIndex == -1 && method.getParameterTypes().length == 1) {
            throw new RuntimeException(String.format("To apply caching to %s.%s(%s), use the @CacheKey annotation.",
                    method.getDeclaringClass().getName(), method.getName(), method.getParameterTypes()[0].getName()));
        }
    }

    private int getCacheKeyParameterIndex(Method method) {
        if(method.getParameterTypes().length > 0) {
            return 0;
        } else {
            return -1;
        }
    }

}
