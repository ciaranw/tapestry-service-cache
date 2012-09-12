package com.ciaranwood.tapestry.cache.services.integration;

import com.ciaranwood.tapestry.cache.services.*;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ServiceCacheTest {

    private Registry registry;

    @Before
    public void startRegistry() {
        RegistryBuilder builder = new RegistryBuilder();
        builder.add(ServiceCacheModule.class, TestModule.class);
        registry = builder.build();
        registry.performRegistryStartup();
    }

    @Test
    public void testCacheAdvice() {
        TestService service = registry.getService(TestService.class);

        String result = service.cacheThis();

        assertEquals(result, service.cacheThis());
        assertEquals(result, service.cacheThis());
    }

    @Test
    public void testCacheAdviceNotUsedWhenNoAnnotationPresent() {
        TestService service = registry.getService(TestService.class);

        String result = service.dontCacheThis();

        assertNotSame(result, service.dontCacheThis());
    }

    @Test
    public void cannotUseAnnotationWhenMethodHasArguments() {
        InvalidCacheAnnotations invalid = registry.getService(InvalidCacheAnnotations.class);

        try {
            invalid.cantCache("something", "test");
            fail("expected exception");
        } catch(RuntimeException expected) {
            assertTrue(expected.getMessage().contains("Cannot apply caching to " +
                    "public abstract java.lang.String com.ciaranwood.tapestry.cache.services.InvalidCacheAnnotations.cantCache(java.lang.String,java.lang.String), " +
                    "cached methods should have no more than one parameter."));
        }
    }

    @Test
    public void cannotCacheWhenNoInterfaceForService() {
        NoInterface noInterface = registry.getService(NoInterface.class);

        String result = noInterface.cannotCacheThis();

        assertNotSame(result, noInterface.cannotCacheThis());
    }

    @Test
    public void cannotCacheWhenBuiltWithBuilderMethod() {
        BuilderMethodBuiltService builderMethodBuilt = registry.getService(BuilderMethodBuiltService.class);

        String result = builderMethodBuilt.get();

        assertNotSame(result, builderMethodBuilt.get());
    }

    @Test
    public void testUseAnnotatedParameterAsCacheKey() {
        TestService service = registry.getService(TestService.class);

        String result1 = service.cacheThisUsingKey("test1");
        String result2 = service.cacheThisUsingKey("test2");

        assertEquals(result1, service.cacheThisUsingKey("test1"));
        assertEquals(result2, service.cacheThisUsingKey("test2"));
        assertNotSame(service.cacheThisUsingKey("test1"), service.cacheThisUsingKey("test2"));
    }

    @Test
    @Ignore("This test will fail until we can get access to method parameter annotations")
    public void testMissingCacheKeyAnnotation() {
        MissingCacheKey service = registry.getService(MissingCacheKey.class);

        try {
            service.missingCacheKey("test");
            fail("expected exception");
        } catch(RuntimeException expected) {
            assertTrue(expected.getMessage().contains("To apply caching to com.ciaranwood.tapestry.cache.services.MissingCacheKeyImpl.missingCacheKey(java.lang.String), use the @CacheKey annotation."));
        }
    }

    @After
    public void shutdownRegistry() {
        registry.shutdown();
    }
}
