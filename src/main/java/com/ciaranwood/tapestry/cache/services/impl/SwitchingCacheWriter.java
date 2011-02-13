package com.ciaranwood.tapestry.cache.services.impl;

import com.ciaranwood.tapestry.cache.services.CacheLocator;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.AbstractCacheWriter;

import java.util.Collection;

public class SwitchingCacheWriter extends AbstractCacheWriter {

    private final CacheWriterBridges bridges;

    public SwitchingCacheWriter(CacheWriterBridges bridges) {
        this.bridges = bridges;
    }

    @Override
    public void write(Element element) throws CacheException {
        CacheLocator locator = (CacheLocator) element.getKey();

        CacheWriterBridge bridge = bridges.get(locator.getMethodKey());

        Object key = locator.getKeyParameterValue();
        bridge.write(key, element.getObjectValue());
    }

    @Override
    public void writeAll(Collection<Element> elements) throws CacheException {
        for(Element element : elements) {
            write(element);
        }
    }
}
