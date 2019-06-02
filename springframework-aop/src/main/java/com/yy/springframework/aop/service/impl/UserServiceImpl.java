package com.yy.springframework.aop.service.impl;

import com.yy.springframework.aop.model.User;
import com.yy.springframework.aop.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by 2019/6/2.
 */
@Service
public class UserServiceImpl implements UserService {
    public User login(User user) {
        System.out.println("com.yy.springframework.aop.service.impl.UserServiceImpl.login");
//        throw new RuntimeException();
        return user;
    }

    public void userHello() {
        System.out.println("userHello");
    }
}
