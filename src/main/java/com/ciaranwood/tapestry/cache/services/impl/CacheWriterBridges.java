package com.ciaranwood.tapestry.cache.services.impl;

import java.util.HashMap;
import java.util.Map;

public class CacheWriterBridges {

    private final Map<String, CacheWriterBridge> bridges = new HashMap<String, CacheWriterBridge>();

    public CacheWriterBridge get(String methodKey) {
        return bridges.get(methodKey);
    }

    public void put(String methodKey, CacheWriterBridge bridge) {
        bridges.put(methodKey, bridge);
    }

}
