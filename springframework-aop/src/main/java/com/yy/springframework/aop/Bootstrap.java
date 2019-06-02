package com.yy.springframework.aop;

import com.yy.springframework.aop.config.AppConfig;
import com.yy.springframework.aop.model.User;
import com.yy.springframework.aop.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    }
}
