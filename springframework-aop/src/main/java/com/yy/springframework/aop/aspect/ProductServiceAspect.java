package com.yy.springframework.aop.aspect;

import com.yy.springframework.aop.annotation.Auditable;
import com.yy.springframework.aop.model.Product;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/6/2.
 */
@Component
@Aspect
@PropertySource("classpath:common.properties")
@Order(2)
public class ProductServiceAspect {

    @Autowired
    private Environment environment;

    @Pointcut("execution(public * com.yy.springframework.aop.service.ProductService.*(..))")
    private void pc(){}

    @Before(value = "pc() && @annotation(auditable)")
    public void before(JoinPoint joinPoint, Auditable auditable) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        if (args[0] instanceof Product) {
            Product p = (Product)args[0];
            // 从配置文件中获取商品类型
            String type = environment.getProperty(auditable.value());
            p.setType(Integer.parseInt(type));
        }
    }
}
