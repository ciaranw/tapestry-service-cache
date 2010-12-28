package com.ciaranwood.tapestry.cache.services;

import org.apache.tapestry5.ioc.ServiceBinder;

public final class WriteThroughModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(StringStore.class);
        binder.bind(StringDao.class, StringStoreDao.class);
    }
}
