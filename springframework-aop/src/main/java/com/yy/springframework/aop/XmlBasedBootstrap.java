package com.yy.springframework.aop;

import com.yy.springframework.aop.model.User;
import com.yy.springframework.aop.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/6/22.
 */
public class XmlBasedBootstrap {
    public static void main(String args[]){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application4.xml");
        UserService userService = context.getBean(UserService.class);
        User user = new User();
        user.setId(-1L);
        userService.login(user);
    }
}
