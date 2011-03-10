package com.ciaranwood.tapestry.cache.services.integration;

import com.ciaranwood.tapestry.cache.services.ServiceCacheModule;
import com.ciaranwood.tapestry.cache.services.TestModule;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.services.TapestryModule;
import org.junit.Test;

public class TapestryCoreTest {

    @Test
    public void testSuccessfulStartupWithTapestryCoreModule() {
        RegistryBuilder builder = new RegistryBuilder();
        builder.add(ServiceCacheModule.class, TestModule.class, TapestryModule.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();
    }
}
