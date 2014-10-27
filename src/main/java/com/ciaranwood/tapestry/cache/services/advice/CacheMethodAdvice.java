package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.CacheLocator;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.slf4j.Logger;

public class CacheMethodAdvice implements MethodAdvice {

    private final Ehcache cache;
    private final Logger log;
    private final int cacheKeyParameterIndex;
    private final String methodKey;

    public CacheMethodAdvice(Ehcache cache, int cacheKeyParameterIndex, String methodKey, Logger log) {
        this.cache = cache;
        this.log = log;
        this.cacheKeyParameterIndex = cacheKeyParameterIndex;
        this.methodKey = methodKey;
    }

    public void advise(MethodInvocation invocation) {
        CacheLocator key = getKeyForInvocation(invocation);
        Element cached = cache.get(key);
        if(cached == null) {
            log.debug("cache miss for {} using cache {}", key, cache.getName());
            try
			{
				invocation.proceed();
				Object result = invocation.getReturnValue();
				cache.put(new Element(key, result));
			}
			catch (Throwable throwable)
			{
				cache.put(new Element(key, null));
				throw throwable;
			}
            log.debug("cached result of {} using cache {}", key, cache.getName());
        } else {
            invocation.setReturnValue(cached.getValue());
            log.debug("cache hit for {} using cache {}", key, cache.getName());
        }
    }

    private CacheLocator getKeyForInvocation(MethodInvocation invocation) {
        if(cacheKeyParameterIndex == -1) {
            return new CacheLocator(methodKey);
        } else {
            Object key = invocation.getParameter(cacheKeyParameterIndex);
            return new CacheLocator(methodKey, key);
        }
    }

}
