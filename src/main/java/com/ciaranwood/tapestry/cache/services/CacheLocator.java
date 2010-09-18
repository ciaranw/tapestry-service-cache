package com.ciaranwood.tapestry.cache.services;

public class CacheLocator {
    private final String methodName;
    private final Object keyParameterValue;

    public CacheLocator(String methodName, Object keyParameterValue) {
        this.methodName = methodName;
        this.keyParameterValue = keyParameterValue;
    }

    public CacheLocator(String methodName) {
        this(methodName, null);
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
            return methodName.equals(key.methodName);
        }

    }

    @Override
    public int hashCode() {
        int result = methodName.hashCode();
        result = 31 * result + (keyParameterValue != null ? keyParameterValue.hashCode() : 0);
        return result;
    }
}

