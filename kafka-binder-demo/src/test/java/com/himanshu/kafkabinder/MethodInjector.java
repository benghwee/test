package com.himanshu.kafkabinder;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

public class MethodInjector {
    public static void injectMethod(Class<?> targetClass, String methodName, String methodBody) throws Exception {
        // Use Byte Buddy to create a dynamic subclass with the injected method
        Class<?> dynamicClass = new ByteBuddy()
                .subclass(targetClass)
                .method(isDeclaredBy(targetClass))
                .intercept(FixedValue.value(methodBody))
                .make()
                .load(targetClass.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        // Get the injected method
        Method injectedMethod = dynamicClass.getDeclaredMethod(methodName);

        // Invoke the injected method
        injectedMethod.invoke(dynamicClass.newInstance());
    }

    public static void main(String[] args) throws Exception {
        // Example usage:
        Class<?> targetClass = MyClass.class;
        String methodName = "newMethod";
        String methodBody = "System.out.println(\"Injected method body\");";

        injectMethod(targetClass, methodName, methodBody);
    }
}
