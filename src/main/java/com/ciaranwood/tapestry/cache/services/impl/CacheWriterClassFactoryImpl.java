package com.ciaranwood.tapestry.cache.services.impl;

import com.ciaranwood.tapestry.cache.services.CacheWriterClassFactory;
import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
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
    private final Map<String, CacheWriter> createdWriters = new HashMap<String, CacheWriter>();

    public CacheWriterClassFactoryImpl(ClassFactory classFactory) {
        this.classFactory = classFactory;
    }

    public <T> void add(Ehcache cache, Class<T> serviceInterface, T instance, Method write) {
        ClassFab classFab = classFactory.newClass(CacheWriter.class);
        classFab.addField("instance", serviceInterface);
        classFab.addConstructor(new Class[]{ serviceInterface }, null, "instance = $1;");

        String cloneBody = String.format("throw new %s();", CloneNotSupportedException.class.getName());
        classFab.addMethod(Modifier.PUBLIC,
                new MethodSignature(getMethodFromCacheWriter("clone", Ehcache.class)),
                cloneBody);

        String writeBody = String.format("instance.%s((%s) $1.getObjectKey(), (%s) $1.getObjectValue());",
                write.getName(), write.getParameterTypes()[0].getName(), write.getParameterTypes()[1].getName());
        classFab.addMethod(Modifier.PUBLIC,
                new MethodSignature(getMethodFromCacheWriter("write", Element.class)),
                writeBody);

        BodyBuilder writeAllBody = new BodyBuilder();
        writeAllBody.begin();
        writeAllBody.addln("%s iterator = $1.iterator();", Iterator.class.getName());
        writeAllBody.addln("while(iterator.hasNext())");
        writeAllBody.begin();
        writeAllBody.addln("write((%s) iterator.next());", Element.class.getName());
        writeAllBody.end();
        writeAllBody.end();
        classFab.addMethod(Modifier.PUBLIC,
                new MethodSignature(getMethodFromCacheWriter("writeAll", Collection.class)),
                writeAllBody.toString());

        classFab.addNoOpMethod(new MethodSignature(getMethodFromCacheWriter("init")));
        classFab.addNoOpMethod(new MethodSignature(getMethodFromCacheWriter("dispose")));

        String exceptionBody = String.format("throw new %s();", CacheException.class.getName());

        classFab.addMethod(Modifier.PUBLIC,
                new MethodSignature(getMethodFromCacheWriter("delete", CacheEntry.class)),
                exceptionBody);

        classFab.addMethod(Modifier.PUBLIC,
                new MethodSignature(getMethodFromCacheWriter("deleteAll", Collection.class)),
                exceptionBody);

        Class newClass = classFab.createClass();

        try {
            Constructor constructor = newClass.getConstructor(serviceInterface);
            CacheWriter writer = (CacheWriter) constructor.newInstance(instance);
            createdWriters.put(cache.getName(), writer);
            cache.registerCacheWriter(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CacheWriter getCacheWriterForCache(Ehcache cache) {
        return createdWriters.get(cache.getName());
    }

    private Method getMethodFromCacheWriter(String methodName, Class... parameterTypes) {
        try {
            return CacheWriter.class.getMethod(methodName, parameterTypes);
        } catch(NoSuchMethodException e) {
            throw new RuntimeException(String.format("method %s with params %s does not exist on the CacheWriter interface. " +
                    "Check your version of Ehcache is supported.", methodName, Arrays.deepToString(parameterTypes)));
        }
    }
}
