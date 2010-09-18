package com.ciaranwood.tapestry.cache.services.advice;

import java.lang.reflect.Method;
import java.util.Arrays;

public class InterfaceMethodMatcher {

    public static boolean methodsMatch(Method interfaceMethod, Method implementationMethod) {
        if(interfaceMethod.getDeclaringClass().isAssignableFrom(implementationMethod.getDeclaringClass())) {
            boolean namesMatch = interfaceMethod.getName().equals(implementationMethod.getName());
            boolean argsMatch = Arrays.equals(interfaceMethod.getParameterTypes(), implementationMethod.getParameterTypes());
            return namesMatch && argsMatch;
        } else {
            return false;
        }
    }

    public static boolean implementsInterfaceMethod(Method implementationMethod, Class<?> interfaceClass) {
        for(Method interfaceMethod : interfaceClass.getDeclaredMethods()) {
            if(methodsMatch(interfaceMethod, implementationMethod)) {
                return true;
            }
        }

        return false;
    }

    public static Method findInterfaceMethod(Method implementationMethod, Class<?> interfaceClass) {
        for(Method interfaceMethod : interfaceClass.getDeclaredMethods()) {
            if(methodsMatch(interfaceMethod, implementationMethod)) {
                return interfaceMethod;
            }
        }

        throw new RuntimeException(String.format("could not find method %s.%s on interface %s",
                implementationMethod.getDeclaringClass().getName(),
                implementationMethod.getName(),
                interfaceClass.getName()));
    }

}
