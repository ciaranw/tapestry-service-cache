package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.CacheFactory;
import com.ciaranwood.tapestry.cache.services.CacheWriterClassFactory;
import com.ciaranwood.tapestry.cache.services.impl.SwitchingCacheWriter;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.AspectDecorator;
import org.apache.tapestry5.ioc.services.AspectInterceptorBuilder;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class CacheMethodDecoratorImpl implements CacheMethodDecorator {

    private final AspectDecorator decorator;
    private final CacheFactory cacheFactory;
    private final CacheWriterClassFactory classFactory;
    private static final String DEFAULT_ANNOTATION_VALUE = "";

    public CacheMethodDecoratorImpl(AspectDecorator decorator, CacheFactory cacheFactory, CacheWriterClassFactory classFactory) {
        this.decorator = decorator;
        this.cacheFactory = cacheFactory;
        this.classFactory = classFactory;
    }

    public <T> T build(T delegate, ServiceResources resources) {

        Class<T> serviceInterface = resources.getServiceInterface();

        AspectInterceptorBuilder<T> builder = decorator.createBuilder(serviceInterface, delegate,
                String.format("<Cache interceptor for %s(%s)", resources.getServiceId(), serviceInterface.getName()));

        for(CachedMethodPair methodPair : new CachedMethodPairsLocator(serviceInterface, resources)) {

            Ehcache cache = getCache(methodPair.getCacheName(), resources.getServiceId());
            CacheAdviceBuilder adviceBuilder = new CacheAdviceBuilder<T>(delegate, resources, cache);
            String methodKey = methodPair.getMethodKey();

            Method read = methodPair.getReadMethod();

            if(read != null) {
                Method method = getMethodToAdvise(serviceInterface, read);
                builder.adviseMethod(method, adviceBuilder.buildReadMethodAdvice(methodKey, read));
            }

            Method write = methodPair.getWriteMethod();
            
            if(write != null) {
                Method method = getMethodToAdvise(serviceInterface, write);
                builder.adviseMethod(method, adviceBuilder.buildWriteMethodAdvice(methodKey, write));
                classFactory.add(cache, serviceInterface, delegate, write, methodKey);
                CacheWriter writer = new SwitchingCacheWriter(classFactory.getCacheWriterBridges(cache));
                cache.registerCacheWriter(writer);
            }
        }

        return builder.build();
    }

    private Method getMethodToAdvise(Class<?> serviceInterface, Method implementationMethod) {
        try {
            return serviceInterface.getMethod(implementationMethod.getName(), implementationMethod.getParameterTypes());
        } catch(NoSuchMethodException e) {
            throw new RuntimeException(getNonServiceMethodMessage(implementationMethod));
        }
    }

    private String getNonServiceMethodMessage(Method method) {
        return String.format("A @CacheResult annotation is present on %s.%s(). " +
                "Please remove this annotation as caching is not supported on " +
                "methods that are not declared by service interfaces.",
                method.getDeclaringClass().getName(), method.getName());
    }

    private Ehcache getCache(String annotationCacheName, String serviceId) {
        String cacheName;

        if(DEFAULT_ANNOTATION_VALUE.equals(annotationCacheName)) {
            cacheName = serviceId;
        } else {
            cacheName = annotationCacheName;
        }

        return cacheFactory.getCache(cacheName);
    }

}
