package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.advice.CacheMethodDecorator;
import com.ciaranwood.tapestry.cache.services.advice.CacheMethodDecoratorImpl;
import com.ciaranwood.tapestry.cache.services.impl.BlockingCacheFactory;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.slf4j.Logger;

public final class ServiceCacheModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(CacheFactory.class, BlockingCacheFactory.class);
        binder.bind(CacheMethodDecorator.class, CacheMethodDecoratorImpl.class);
    }

    @Match("*")
    public static <T> T decorateForCaching(Class<T> serviceInterface, T delegate, String serviceId,
                                           CacheMethodDecorator decorator, Logger log) {
        return decorator.build(serviceInterface, delegate, serviceId, log);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(ServiceCacheConstants.BLOCKING_CACHE_TIMEOUT, "30000");
        configuration.add(ServiceCacheConstants.EHCACHE_CONFIGURATION_FILE, "/META-INF/ehcache-default.xml");
    }
}
