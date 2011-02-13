package com.ciaranwood.tapestry.cache.services.impl;

import com.ciaranwood.tapestry.cache.services.CacheWriterClassFactory;
import net.sf.ehcache.Ehcache;
import org.apache.tapestry5.ioc.services.ClassFab;
import org.apache.tapestry5.ioc.services.ClassFactory;
import org.apache.tapestry5.ioc.services.MethodSignature;
import org.apache.tapestry5.ioc.util.BodyBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class CacheWriterClassFactoryImpl implements CacheWriterClassFactory {

    private final ClassFactory classFactory;
    private final Map<String, CacheWriterBridges> bridgesToCacheName = new HashMap<String, CacheWriterBridges>();

    public CacheWriterClassFactoryImpl(ClassFactory classFactory) {
        this.classFactory = classFactory;
    }

    public <T> void add(Ehcache cache, Class<T> serviceInterface, T instance, Method write, String methodKey) {
        ClassFab classFab = classFactory.newClass(CacheWriterBridge.class);
        classFab.addField("instance", serviceInterface);
        classFab.addConstructor(new Class[]{ serviceInterface }, null, "instance = $1;");

        BodyBuilder writeBody = new BodyBuilder();
        writeBody.begin();
        writeBody.addln("instance.%s((%s) $1, (%s) $2);",
                write.getName(), write.getParameterTypes()[0].getName(), write.getParameterTypes()[1].getName());
        writeBody.end();
        classFab.addMethod(Modifier.PUBLIC,
                new MethodSignature(void.class, "write", new Class[] {Object.class, Object.class}, null),
                writeBody.toString());

        Class newClass = classFab.createClass();

        try {
            Constructor constructor = newClass.getConstructor(serviceInterface);
            CacheWriterBridge bridge = (CacheWriterBridge) constructor.newInstance(instance);
            putBridge(cache.getName(), methodKey, bridge);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public CacheWriterBridges getCacheWriterBridges(Ehcache cache) {
        return bridgesToCacheName.get(cache.getName());
    }

    private void putBridge(String cacheName, String methodKey, CacheWriterBridge bridge) {
        CacheWriterBridges bridges = bridgesToCacheName.get(cacheName);

        if(bridges == null) {
            bridges = new CacheWriterBridges();
            bridgesToCacheName.put(cacheName, bridges);
        }

        bridges.put(methodKey, bridge);
    }

}
