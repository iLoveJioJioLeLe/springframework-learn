package com.yy.springframework.aop.aspect;

import com.yy.springframework.aop.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Created by 2019/6/2.
 */
@Aspect
@Component
public class UserServiceAspect {
    @Pointcut("execution(public * com.yy.springframework.aop.service.UserService.*(..))")
    public void pointcut1(){}

    @Before("pointcut1() && args(user)")
//    @Before("execution(public * com.yy.springframework.aop.service.*.*(..))")
    public void before(JoinPoint joinPoint, User user) {
//        System.out.println(joinPoint);
//        System.out.println(user);
        System.out.println("before");
    }

    @After("pointcut1()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning(value = "pointcut1()", returning = "user")
    public void afterReturning(User user) {
        System.out.println("afterReturning");
    }

    @AfterThrowing(pointcut = "pointcut1()", throwing = "ex")
    public void afterThrowing(Exception ex) {
        System.out.println("afterThrowing");
        System.out.println(ex);
    }

    @Around("pointcut1()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        StopWatch clock = new StopWatch(point.getSignature().toString());
        try {
            clock.start(point.toShortString());
            System.out.println("around before");
            User user = new User();
            user.setId(1L);
            Object[] args = new Object[]{user};
            Object o = point.proceed(args);
            System.out.println("around after");
            return o;
        } finally {
            clock.stop();
            System.out.println(clock.prettyPrint());
        }
    }
}
