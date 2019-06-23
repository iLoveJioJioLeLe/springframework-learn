package com.yy.springframework.aop.service.impl;

import com.yy.springframework.aop.annotation.TimeCount;
import com.yy.springframework.aop.model.User;
import com.yy.springframework.aop.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

/**
 * Created by 2019/6/2.
 */
@Service
public class UserServiceImpl implements UserService {
    @TimeCount
    public User login(User user) {
        System.out.println("com.yy.springframework.aop.service.impl.UserServiceImpl.login");
        if (user.getId() < 0) {
            throw new RuntimeException("登录失败");
        }
        return user;
    }

    public User register(User user) {
        System.out.println("com.yy.springframework.aop.service.impl.UserServiceImpl.register");
        // 获取代理对象 会进入advice方法
        return ((UserService) AopContext.currentProxy()).login(user);
        // 目标对象 不会进入advice方法
//        return this.login(user);
    }

    public void userHello() {
        System.out.println("userHello");
    }
}
