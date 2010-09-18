package com.ciaranwood.tapestry.cache.services.advice;

import org.slf4j.Logger;

public interface CacheMethodDecorator {

    <T> T build(Class<T> serviceInterface, T delegate, String serviceId, Logger log);
}
