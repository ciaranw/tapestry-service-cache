package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.CacheFactory;
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

    public WriteThroughAdvice(Ehcache cache, int cacheKeyParameterIndex, Logger log) {
        this.cache = cache;
        this.log = log;
        this.cacheKeyParameterIndex = cacheKeyParameterIndex;
        this.dataParameterIndex = (cacheKeyParameterIndex == 0) ? 1 : 0;
    }

    public void advise(Invocation invocation) {
        Object key = invocation.getParameter(cacheKeyParameterIndex);
        Object data = invocation.getParameter(dataParameterIndex);

        cache.putWithWriter(new Element(key, data));
    }
}
