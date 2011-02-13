package com.ciaranwood.tapestry.cache.services;

import org.apache.tapestry5.ioc.ServiceBinder;

public final class WriteThroughModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(StringStore.class);
        binder.bind(IntegerStore.class);
        binder.bind(MultipleDao.class, MultipleStoreDao.class);
        binder.bind(StringDao.class, StringStoreDao.class);
        binder.bind(AlternateCacheKeyIndex.class, AlternateCacheKeyIndexImpl.class);
        binder.bind(MissingCacheResultForWriteThrough.class, MissingCacheResultForWriteThroughImpl.class);
    }
}
