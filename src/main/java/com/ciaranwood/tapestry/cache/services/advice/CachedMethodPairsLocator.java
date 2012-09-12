package com.ciaranwood.tapestry.cache.services.advice;

import com.ciaranwood.tapestry.cache.services.annotations.CacheResult;
import com.ciaranwood.tapestry.cache.services.annotations.WriteThrough;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.ServiceResources;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CachedMethodPairsLocator implements Iterable<CachedMethodPair> {

    private final Map<String, CachedMethodPair> methodKeyToMethodPair = new HashMap<String, CachedMethodPair>();

    public CachedMethodPairsLocator(Class<?> serviceInterface, ServiceResources resources) {
        for(Method method : serviceInterface.getDeclaredMethods()) {
            AnnotationProvider provider = resources.getMethodAnnotationProvider(method.getName(), method.getParameterTypes());
            CacheResult cacheResult = provider.getAnnotation(CacheResult.class);
            if(cacheResult != null) {
                addReadMethod(method, cacheResult);
            }

            WriteThrough writeThrough = provider.getAnnotation(WriteThrough.class);
            if(writeThrough != null) {
                addWriteMethod(method, writeThrough);
            }
        }
    }

    private void addReadMethod(Method read, CacheResult annotation) {
        String methodKey = getMethodKeyForRead(read, annotation);

        CachedMethodPair methodPair = getMethodPair(methodKey);
        methodPair.setRead(read, annotation);
    }

    private CachedMethodPair getMethodPair(String methodKey) {
        CachedMethodPair methodPair = methodKeyToMethodPair.get(methodKey);
        if(methodPair == null) {
            methodPair = new CachedMethodPair(methodKey);
            methodKeyToMethodPair.put(methodKey, methodPair);
        }

        return methodPair;
    }

    private String getMethodKeyForRead(Method method, CacheResult annotation) {
        String annotationValue = annotation.value();
        if(annotationValue.equals("")) {
            return method.getName();
        } else {
            return annotationValue;
        }
    }

    private void addWriteMethod(Method write, WriteThrough annotation) {
        String methodKey = getMethodKeyForWrite(write, annotation);

        CachedMethodPair methodPair = getMethodPair(methodKey);
        methodPair.setWrite(write, annotation);
    }

    private String getMethodKeyForWrite(Method write, WriteThrough annotation) {
        String annotationValue = annotation.value();

        if(annotationValue.equals("")) {
            return determineDefaultMethodKey(write);
        } else {
            return annotationValue;
        }
    }

    private String determineDefaultMethodKey(Method write) {
        if(methodKeyToMethodPair.size() == 1) {
            return methodKeyToMethodPair.keySet().iterator().next();
        } else {
            return write.getName();
        }
    }

    public Iterator<CachedMethodPair> iterator() {
        return methodKeyToMethodPair.values().iterator();
    }
}
