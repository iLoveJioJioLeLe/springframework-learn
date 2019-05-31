package com.yy.springframework.validate.service;

import com.yy.springframework.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Created by 2019/5/31.
 */
@Validated
@Service
public class UserService {

    public User getUserById(@Valid User user) {
        return user;
    }
}
