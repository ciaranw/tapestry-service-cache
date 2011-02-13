package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.CacheFactory;
import com.ciaranwood.tapestry.cache.services.CacheLocator;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.slf4j.Logger;

public class WriteThroughAdvice implements MethodAdvice {

    private final Ehcache cache;
    private final Logger log;
    private final int cacheKeyParameterIndex;
    private final int dataParameterIndex;
    private final String methodKey;

    public WriteThroughAdvice(Ehcache cache, String methodKey, int cacheKeyParameterIndex, Logger log) {
        this.cache = cache;
        this.log = log;
        this.cacheKeyParameterIndex = cacheKeyParameterIndex;
        this.dataParameterIndex = (cacheKeyParameterIndex == 0) ? 1 : 0;
        this.methodKey = methodKey;
    }

    public void advise(Invocation invocation) {
        Object key = invocation.getParameter(cacheKeyParameterIndex);
        Object data = invocation.getParameter(dataParameterIndex);

        CacheLocator locator = new CacheLocator(methodKey, key);

        cache.putWithWriter(new Element(locator, data));
    }
}
