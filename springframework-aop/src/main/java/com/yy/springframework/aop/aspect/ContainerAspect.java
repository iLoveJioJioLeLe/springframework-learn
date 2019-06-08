package com.yy.springframework.aop.aspect;

import com.yy.springframework.aop.model.Container;
import com.yy.springframework.aop.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by 2019/6/7.
 */
@Component
@Aspect
@Order(3)
public class ContainerAspect {

    @Pointcut(value = "execution(* com.yy.springframework.aop.model.Container.get(..))")
    public void pc(){}

    @Before(value = "execution(* com.yy.springframework.aop.model.*.put*(..)) && args(user)")
    public void beforePut(JoinPoint joinPoint, User user) {
        System.out.println(user);
    }

    @Before("execution(* com.yy.springframework.aop.model.*.putAll*(..)) && args(users)")
    public void beforePut(JoinPoint joinPoint, Collection<User> users) {
        System.out.println(users);
    }

    @Before(value = "pc() && target(container)", argNames = "container")
    public void beforeGet(Container container) {
        System.out.println(container.getClass());
    }
}
