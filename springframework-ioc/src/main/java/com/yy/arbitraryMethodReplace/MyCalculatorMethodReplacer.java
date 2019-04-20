package com.yy.arbitraryMethodReplace;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * Created by 2019/4/14.
 */
public class MyCalculatorMethodReplacer implements MethodReplacer {
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {

        return args[0] + " from replacer";
    }
}
