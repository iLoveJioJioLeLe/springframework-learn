package com.yy.springframework.aop.aspect;

import com.yy.springframework.aop.annotation.Auditable;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/6/2.
 */
@Component
@Aspect
public class ProductServiceAspect {

    @Pointcut("execution(public * com.yy.springframework.aop.service.ProductService.*(..))")
    private void pc(){}

    @Before(value = "pc() && @annotation(auditable)")
    public void before(Auditable auditable) {

    }
}
