package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.CacheLocator;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
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

    public void advise(MethodInvocation invocation) {
        Object key = invocation.getParameter(cacheKeyParameterIndex);
        Object data = invocation.getParameter(dataParameterIndex);

        CacheLocator locator = new CacheLocator(methodKey, key);

        cache.putWithWriter(new Element(locator, data));
    }
}
