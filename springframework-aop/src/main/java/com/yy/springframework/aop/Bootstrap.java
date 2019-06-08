package com.yy.springframework.aop;

import com.yy.springframework.aop.config.AppConfig;
import com.yy.springframework.aop.model.Container;
import com.yy.springframework.aop.model.Product;
import com.yy.springframework.aop.model.User;
import com.yy.springframework.aop.service.IdGeneratorService;
import com.yy.springframework.aop.service.ProductService;
import com.yy.springframework.aop.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * Created by 2019/6/2.
 */
public class Bootstrap {
    public static void main(String args[]){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User user = new User();
        user.setId(2L);
        userService.login(user);

        // advice方法接收自定义注解
        ProductService productService = context.getBean(ProductService.class);
        productService.createProduct(new Product());

        // advice方法对泛型的处理
        Container userContainer = context.getBean("userContainer", Container.class);
        User user1 = new User();
        user1.setId(1L);
        userContainer.put(user1);

        userContainer.putAll(Arrays.asList(user));

        Container productContainer = context.getBean("productContainer", Container.class);
        Product p1 = new Product();
        p1.setId(1L);
        productContainer.put(p1);// 入参为User类型的advice方法不会进入

        productContainer.putAll(Arrays.asList(p1));// 入参为Collection<User>类型的advice方法会进入!

        productContainer.get(0);

        // Introductions
        ProductService service = context.getBean(ProductService.class);
        IdGeneratorService idGeneratorService = (IdGeneratorService) service;
        System.out.println(idGeneratorService.getId());

    }
}
