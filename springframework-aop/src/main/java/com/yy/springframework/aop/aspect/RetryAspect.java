package com.yy.springframework.aop.aspect;

import com.yy.springframework.aop.annotation.Retry;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * Created by 2019/6/22.
 */
public class RetryAspect implements ThrowsAdvice {

    public void afterThrowing(Method m, Object[] args, Object target, Throwable t) throws Exception {
        Retry retry = m.getAnnotation(Retry.class);
        if (retry != null) {
            m.invoke(target, args);
        }
    }
}
