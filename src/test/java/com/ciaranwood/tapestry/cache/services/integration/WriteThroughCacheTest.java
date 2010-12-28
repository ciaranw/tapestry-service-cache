package com.ciaranwood.tapestry.cache.services.integration;

import com.ciaranwood.tapestry.cache.services.*;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class WriteThroughCacheTest {

    private Registry registry;
    private static final Integer ID = -1;
    private static final String DATA = "Data";

    @Before
    public void startRegistry() {
        RegistryBuilder builder = new RegistryBuilder();
        builder.add(ServiceCacheModule.class, WriteThroughModule.class);
        registry = builder.build();
        registry.performRegistryStartup();
    }

    @Test
    public void testWriteThroughCache() {
        StringDao dao = registry.getService(StringDao.class);
        Ehcache cache = getCache("StringDao");
        StringStore store = registry.getService(StringStore.class);

        assertNull(cache.get(ID));

        dao.put(ID, DATA);

        Element element = cache.get(ID);
        assertNotNull(element);
        assertEquals(DATA, element.getValue());
        assertEquals(DATA, store.get(ID));
    }

    private Ehcache getCache(String serviceId) {
        CacheFactory cacheFactory = registry.getService(CacheFactory.class);
        return cacheFactory.getCache(serviceId);
    }
}
