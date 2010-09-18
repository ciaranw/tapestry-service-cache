package com.ciaranwood.tapestry.cache.services.advice;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InterfaceMethodMatcherTest {

    @Test
    public void methodsMatchNoArgs() throws NoSuchMethodException {
        Method interfaceMethod = TestInterface.class.getMethod("noArgs");
        Method implMethod = TestImpl.class.getMethod("noArgs");

        assertTrue(InterfaceMethodMatcher.methodsMatch(interfaceMethod, implMethod));
    }

    @Test
    public void testMethodsDoNotMatchWhenNameIsDifferent() throws NoSuchMethodException {
        Method interfaceMethod = TestInterface.class.getMethod("noArgs");
        Method implMethod = TestImpl.class.getMethod("alsoNoArgs");

        assertFalse(InterfaceMethodMatcher.methodsMatch(interfaceMethod, implMethod));
    }


    @Test
    public void methodsMatchUsingNameAndArgs() throws NoSuchMethodException {
        Method interfaceMethod = TestInterface.class.getMethod("oneArg", String.class);
        Method implMethod = TestImpl.class.getMethod("oneArg", String.class);

        assertTrue(InterfaceMethodMatcher.methodsMatch(interfaceMethod, implMethod));
    }

    @Test
    public void methodsDoNotMatchWhenArgsAreDifferent() throws NoSuchMethodException {
        Method interfaceMethod = TestInterface.class.getMethod("oneArg", String.class);
        Method implMethod = TestImpl.class.getMethod("oneArg", Long.class);

        assertFalse(InterfaceMethodMatcher.methodsMatch(interfaceMethod, implMethod));
    }

    @Test
    public void methodsDoNotMatchWhenNameAndArgsAreDifferent() throws NoSuchMethodException {
        Method interfaceMethod = TestInterface.class.getMethod("oneArg", String.class);
        Method implMethod = TestImpl.class.getMethod("manyArgs", String.class, int.class);

        assertFalse(InterfaceMethodMatcher.methodsMatch(interfaceMethod, implMethod));
    }

    @Test
    public void methodsDoNotMatchWhenArgsHaveDifferentOrder() throws NoSuchMethodException {
        Method interfaceMethod = TestInterface.class.getMethod("manyArgs", String.class, int.class);
        Method implMethod = TestImpl.class.getMethod("manyArgs", int.class, String.class);

        assertFalse(InterfaceMethodMatcher.methodsMatch(interfaceMethod, implMethod));
    }

    @Test
    public void returnFalseWhenSameSignatureButNotASubclass() throws NoSuchMethodException {
        Method interfaceMethod = TestInterface.class.getMethod("noArgs");
        Method implMethod = TestImpl2.class.getMethod("noArgs");

        assertFalse(InterfaceMethodMatcher.methodsMatch(interfaceMethod, implMethod));

    }


    private interface TestInterface {

        void noArgs();

        void alsoNoArgs();

        void oneArg(String arg);

        void oneArg(Long arg);

        void manyArgs(String arg1, int arg2);

        void manyArgs(int arg1, String arg2);

    }

    private class TestImpl implements TestInterface {
        public void noArgs() {
        }

        public void alsoNoArgs() {
        }

        public void oneArg(String arg) {
        }

        public void oneArg(Long arg) {
        }

        public void manyArgs(String arg1, int arg2) {
        }

        public void manyArgs(int arg1, String arg2) {
        }

    }

    private class TestImpl2 {
        public void noArgs() {}
    }

}
