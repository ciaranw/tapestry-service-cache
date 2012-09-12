package com.ciaranwood.tapestry.cache.services.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation to specify the annotated parameter should be used as a discriminator in the cache key.
 * Deprecated in 5.3 because there is no access to method parameter annotations from service proxies
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface CacheKey {
}
