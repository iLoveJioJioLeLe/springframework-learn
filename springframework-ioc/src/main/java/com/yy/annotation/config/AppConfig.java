package com.yy.annotation.config;

import com.yy.annotation.annotations.CustomProfile;
import com.yy.annotation.beans.Book;
import com.yy.annotation.beans.UserDao;
import com.yy.annotation.beans.UserDaoImpl;
import com.yy.annotation.beans.UserService;
import com.yy.annotation.condition.CustomCondition;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/3.
 */
@Configuration
//@Component
@ComponentScan(basePackages = {"com.yy.annotation"})
public class AppConfig {
    @Bean
    public Book aBook() {
        return new Book();
    }

    @Bean
    public UserService userService1() {
        return new UserService(userDao());
    }

    @Bean
    @CustomProfile(2)
    public UserService userService2() {
        return new UserService(userDao());
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoImpl();
    }
}
