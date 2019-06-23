package com.yy.springframework.aop.aspect;

import com.yy.springframework.aop.annotation.TimeCount;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * Created by 2019/6/22.
 */
public class TimeCountAspect {

    private static final Logger logger = Logger.getLogger(TimeCountAspect.class);

    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = new Class[joinPoint.getArgs().length];
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            parameterTypes[i] = joinPoint.getArgs()[i].getClass();
        }
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        if (method.getAnnotation(TimeCount.class) != null) {
            logger.info(joinPoint.getSignature().getName() + "花费时间" + (System.currentTimeMillis() - startTime));
        }
        return obj;
    }
}
