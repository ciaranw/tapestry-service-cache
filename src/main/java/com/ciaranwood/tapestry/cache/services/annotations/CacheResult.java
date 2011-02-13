package com.ciaranwood.tapestry.cache.services.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation to specify that a method result should be cached. Place this on the implementation method,
 * not the interface.  
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface CacheResult {

    /**
     * Discrimiator used to identify cached values from a particular method. Set this to the same value as a
     * corresponding @WriteThrough value to read values from that write-through cache.
     */
    String value() default "";

    String cacheName() default "";
}
