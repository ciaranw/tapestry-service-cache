package com.ciaranwood.tapestry.cache.services;

import java.io.Serializable;

public class CacheLocator implements Serializable {
    private final String methodKey;
    private final Object keyParameterValue;

    public CacheLocator(String methodKey, Object keyParameterValue) {
        this.methodKey = methodKey;
        this.keyParameterValue = keyParameterValue;
    }

    public CacheLocator(String methodKey) {
        this(methodKey, null);
    }

    public Object getKeyParameterValue() {
        return keyParameterValue;
    }

    public String getMethodKey() {
        return methodKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheLocator key = (CacheLocator) o;

        if (keyParameterValue != null ? !keyParameterValue.equals(key.keyParameterValue) :
                key.keyParameterValue != null) {
            return false;
        } else {
            return methodKey.equals(key.methodKey);
        }

    }

    @Override
    public int hashCode() {
        int result = methodKey.hashCode();
        result = 31 * result + (keyParameterValue != null ? keyParameterValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CacheLocator{");
        builder.append(methodKey);
        builder.append("(");

        if(keyParameterValue != null) {
            builder.append(keyParameterValue);
        }

        builder.append(")}");

        return builder.toString();
    }
}

