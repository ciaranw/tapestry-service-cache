# Tapestry Service Cache

Tapestry Service Cache is an addon library for Tapestry 5 that allows you to add simple, annotation based caching to
your project. Due to the modular, plug and play nature of Tapestry 5, you can just add this library as a dependency
and add some annotations to your service classes to make your services cachable!

The easiest way to use this library is to use maven. Add this to your pom:

```
<dependency>
    <groupId>com.ciaranwood</groupId>
    <artifactId>tapestry-service-cache</artifactId>
    <version>3.0</version>
</dependency>
```

Version 1.0 is compatible with Tapestry 5.1, version 2.0.1 is compatible with Tapestry 5.2 and version 3.0 is compatible with Tapestry 5.3.

Tapestry Service Cache is an add-on library for Tapestry 5 that makes caching of services simple. It uses annotations to mark methods as cacheable and takes care of all the heavy lifting for you.

## How To Use
Tapestry Service Cache has a few requirements for services it can cache:

* The service to cache must implement an interface - this is so Tapestry can generate a proxy to the service implementation and allow the service to be decorated.
* The method(s) you would like to cache should have 0 or 1 method parameter(s). If you have one parameter to your method, the value of that method is used as a discriminator in the key of the cached value.

You enable caching by putting a `@CacheResult` annotation on the *implementation* method. This will cache the result of the method invocation in a cache with the same name as the id of the service. The `@CacheResult` annotation can also take a parameter, cacheName. This value will be used as the name of the cache in which the values are stored, instead of the service id. If your method has a single parameter then it will be used as the cache key.

```java
  import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;

  public class CachedService implements Service {

    @CacheResult
    public String expensiveOperation() {
      //some expensive operation here
    }

    @CacheResult
    public String expensiveOperation(Integer key) {
      //This uses the key parameter as a discriminator in the cache key, so
      //invocations with different parameters are cached under different keys.
    }
  }
```

## Frequently Asked Questions
** Q. My method is not being cached, but I added `@CacheResult` to the method I want to cache! **

A. Most likely, you have placed the annotation on the interface. This is not supported (`@CacheResult` must go on the implementation method, not the interface). If the service does not have an interface, you must add one, as Tapestry does not support advice around services that do not have an interface.

Also, you may have put the annotation on a method that is not part of the service interface. Tapestry always creates a proxy when the service implements an interface. This is how we add the caching functionality, so therefore we cannot add this without the Tapestry proxy.

** Q. How can I set custom settings for my cache? **

A. By default, tapestry-service-cache uses the defaultCache settings from the ehcache-default.xml file found in the library's jar file. You can use your own custom ehcache.xml file however. Simply contribute to the ApplicationDefaults service in your Module class using this key: `com.ciaranwood.tapestry.cache.ehcache-configuration-file`. You can use the `ServiceCacheConstants.EHCACHE_CONFIGURATION_FILE` static constant. By default, tapestry-service-cache will use the service id as the cache name, unless an explicit `cacheName` is provided with the `@CacheResult` annotation.