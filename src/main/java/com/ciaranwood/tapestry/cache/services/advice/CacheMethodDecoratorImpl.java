package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.CacheFactory;
import com.ciaranwood.tapestry.cache.services.annotations.CacheKey;
import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import org.apache.tapestry5.ioc.services.AspectDecorator;
import org.apache.tapestry5.ioc.services.AspectInterceptorBuilder;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CacheMethodDecoratorImpl implements CacheMethodDecorator {

    private final AspectDecorator decorator;
    private final CacheFactory cacheFactory;

    public CacheMethodDecoratorImpl(AspectDecorator decorator, CacheFactory cacheFactory) {
        this.decorator = decorator;
        this.cacheFactory = cacheFactory;
    }

    public <T> T build(Class<T> serviceInterface, T delegate, String serviceId, Logger log) {

        AspectInterceptorBuilder<T> builder = decorator.createBuilder(serviceInterface, delegate,
                String.format("<Cache interceptor for %s(%s)", serviceId, serviceInterface.getName()));

        for(Method method : delegate.getClass().getDeclaredMethods()) {

            CacheResult annotation = method.getAnnotation(CacheResult.class);

            if(annotation != null) {
                checkInterfaceDefinesMethod(method, serviceInterface);
                String cacheName = determineCacheName(annotation, serviceId);
                int cacheKeyIndex = getCacheKeyParameterIndex(method);

                checkForIncorrectParameterCount(method);
                validateCacheKeyAnnotation(method, cacheKeyIndex);

                Method toAdvise = InterfaceMethodMatcher.findInterfaceMethod(method, serviceInterface);
                builder.adviseMethod(toAdvise, new CacheMethodAdvice(cacheName, cacheKeyIndex, cacheFactory, log));
            }
        }

        return builder.build();
    }

    private void checkInterfaceDefinesMethod(Method method, Class<?> serviceInterface) {
        if(!InterfaceMethodMatcher.implementsInterfaceMethod(method, serviceInterface)) {
            throw new RuntimeException(String.format("A @CacheResult annotation is present on %s.%s(). " +
                    "Please remove this annotation as caching is not supported on " +
                    "methods that are not declared by service interfaces.", method.getDeclaringClass().getName(), method.getName()));
        }
    }

    private void checkForIncorrectParameterCount(Method method) {
        if(method.getParameterTypes().length > 1) {
            throw new RuntimeException(String.format("Cannot apply caching to %s, cached methods should have no more than one parameter.",
                    method));
        }
    }

    private void validateCacheKeyAnnotation(Method method, int cacheKeyIndex) {
        if(cacheKeyIndex == -1 && method.getParameterTypes().length == 1) {
            throw new RuntimeException(String.format("To apply caching to %s.%s(%s), use the @CacheKey annotation.",
                    method.getDeclaringClass().getName(), method.getName(), method.getParameterTypes()[0].getName()));
        }
    }


    private String determineCacheName(CacheResult annotation, String serviceId) {
        if(annotation.cacheName().equals("")) {
            return serviceId;
        } else {
            return annotation.cacheName();
        }
    }

    private int getCacheKeyParameterIndex(Method method) {
        Annotation[][] annotations = method.getParameterAnnotations();
        for(int index=0;index<annotations.length;index++) {
            Annotation[] parameterAnnotations = annotations[index];
            if (containsCacheKeyAnnotation(parameterAnnotations)) {
                return index;
            }
        }

        return -1;
    }

    private boolean containsCacheKeyAnnotation(Annotation[] parameterAnnotations) {
        for(Annotation parameterAnnotation : parameterAnnotations) {
            if(parameterAnnotation.annotationType().equals(CacheKey.class)) {
                return true;
            }
        }
        return false;
    }
}
