package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.advice.CacheMethodDecorator;
import com.ciaranwood.tapestry.cache.services.advice.CacheMethodDecoratorImpl;
import com.ciaranwood.tapestry.cache.services.impl.BlockingCacheFactory;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.PreventServiceDecoration;
import org.apache.tapestry5.ioc.services.AspectDecorator;
import org.apache.tapestry5.ioc.services.Builtin;

@PreventServiceDecoration
public final class ServiceCacheModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(CacheFactory.class, BlockingCacheFactory.class);
    }

    public static CacheMethodDecorator buildCacheMethodDecorator(@Builtin AspectDecorator decorator,
                                                                 @Local CacheFactory cacheFactory) {
        return new CacheMethodDecoratorImpl(decorator, cacheFactory);
    }

    @Match("*")
    public static <T> T decorateForCaching(Class<T> serviceInterface, T delegate,
                                           @Local CacheMethodDecorator decorator, ServiceResources resources) {
        return decorator.build(serviceInterface, delegate, resources);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(ServiceCacheConstants.BLOCKING_CACHE_TIMEOUT, "30000");
        configuration.add(ServiceCacheConstants.EHCACHE_CONFIGURATION_FILE, "/META-INF/ehcache-default.xml");
    }
}
