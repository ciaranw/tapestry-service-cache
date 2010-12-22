package com.ciaranwood.tapestry.cache.services.advice;

import org.apache.tapestry5.ioc.ServiceResources;

public interface CacheMethodDecorator {

    <T> T build(Class<T> serviceInterface, T delegate, ServiceResources resources);
}
