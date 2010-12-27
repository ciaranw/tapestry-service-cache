package com.ciaranwood.tapestry.cache.services.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation to specify that the cache should write through to the underlying implementation.
 * Place this on the implementation method, not the interface.  
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface WriteThrough {
}
