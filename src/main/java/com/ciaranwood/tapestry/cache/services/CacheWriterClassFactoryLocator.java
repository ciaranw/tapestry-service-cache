package com.ciaranwood.tapestry.cache.services;

public final class CacheWriterClassFactoryLocator {

    private static CacheWriterClassFactory classFactory;

    private CacheWriterClassFactoryLocator() {}

    public static CacheWriterClassFactory getClassFactory() {
        return classFactory;
    }

    public static void setClassFactory(CacheWriterClassFactory classFactory) {
        CacheWriterClassFactoryLocator.classFactory = classFactory;
    }
}
