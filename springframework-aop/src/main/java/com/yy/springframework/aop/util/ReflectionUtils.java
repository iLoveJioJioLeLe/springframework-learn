package com.yy.springframework.aop.util;

import java.lang.reflect.Method;

/**
 * Created by 2019/6/22.
 */
public class ReflectionUtils {
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        Class superClass = clazz;
        while(superClass != Object.class) {
            try {
                if(superClass == null) {
                    return null;
                }

                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException var5) {
                superClass = superClass.getSuperclass();
            }
        }
        return null;
    }
}
