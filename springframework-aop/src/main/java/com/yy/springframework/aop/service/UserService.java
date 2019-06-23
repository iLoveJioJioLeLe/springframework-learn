package com.yy.springframework.aop.service;

import com.yy.springframework.aop.annotation.Retry;
import com.yy.springframework.aop.model.User;

/**
 * Created by 2019/6/2.
 */
public interface UserService {
    @Retry
    User login(User user);

    User register(User user);
}
