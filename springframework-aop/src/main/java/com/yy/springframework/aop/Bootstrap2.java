package com.yy.springframework.aop;

import com.yy.springframework.aop.config.AppConfig;
import com.yy.springframework.aop.model.User;
import com.yy.springframework.aop.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by 2019/6/8.
 */
public class Bootstrap2 {
    public static void main(String args[]){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.register(new User());
    }
}
