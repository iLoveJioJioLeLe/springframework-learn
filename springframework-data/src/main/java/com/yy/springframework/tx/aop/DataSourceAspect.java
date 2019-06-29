package com.yy.springframework.tx.aop;

import com.yy.springframework.tx.annotation.ChangeDataSource;
import com.yy.springframework.tx.datasource.DynamicDataSourceContext;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * Created by 2019/6/29.
 */
public class DataSourceAspect {

    private final Logger logger = Logger.getLogger(getClass());

    public Object execute(ProceedingJoinPoint joinPoint) {
        Object o = null;
        try {
            Class<?> clazz = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();
            Class[] argClazz = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argClazz[i] = args[i].getClass();
            }
            Method method = clazz.getDeclaredMethod(methodName, argClazz);
            ChangeDataSource classDataSource = clazz.getAnnotation(ChangeDataSource.class);
            ChangeDataSource methodDataSource = method.getAnnotation(ChangeDataSource.class);
            if (methodDataSource != null) {
                logger.info("DataSourceAspect change dataSource to " + methodDataSource.value().getDataSourceName());
                DynamicDataSourceContext.setDataSourceKey(methodDataSource.value().getDataSourceName());
            } else if (classDataSource != null) {
                logger.info("DataSourceAspect change dataSource to " + classDataSource.value().getDataSourceName());
                DynamicDataSourceContext.setDataSourceKey(classDataSource.value().getDataSourceName());
            }
            o = joinPoint.proceed();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return o;
    }
}
