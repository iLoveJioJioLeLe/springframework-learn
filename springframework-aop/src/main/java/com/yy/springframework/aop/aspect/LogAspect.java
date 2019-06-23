package com.yy.springframework.aop.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * Created by 2019/6/22.
 */
public class LogAspect implements MethodInterceptor {

    private static final Logger logger = Logger.getLogger(LogAspect.class);

    public Object invoke(MethodInvocation invocation) throws Throwable {
        StringBuilder sb = new StringBuilder();
        for (Object o : invocation.getArguments()) {
            sb.append(o);
        }
        logger.info(invocation.getMethod() + "开始执行，入参：" + sb.toString());
        Object obj = invocation.proceed();
        logger.info(invocation.getMethod() + "结束执行，出参：" + obj);
        return obj;
    }
}
