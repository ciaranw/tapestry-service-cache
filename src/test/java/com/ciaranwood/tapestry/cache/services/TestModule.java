package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import org.apache.tapestry5.ioc.ServiceBinder;

public final class TestModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(TestService.class, TestServiceImpl.class);
        binder.bind(InvalidCacheAnnotations.class, InvalidCacheAnnotationsImpl.class);
        binder.bind(AnotherTestService.class, AnotherTestServiceImpl.class);
        binder.bind(MissingCacheKey.class, MissingCacheKeyImpl.class);
        binder.bind(NoInterface.class);
    }

    public static BuilderMethodBuiltService buildBuilderMethodBuiltService() {
        return new BuilderMethodBuiltService() {

            @CacheResult
            public String get() {
                return "BuilderMethodBuiltService:" + System.currentTimeMillis();
            }
        };
    }
}
