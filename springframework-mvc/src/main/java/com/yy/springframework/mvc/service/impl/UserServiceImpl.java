package com.yy.springframework.mvc.service.impl;

import com.yy.springframework.mvc.mapper.UserMapper;
import com.yy.springframework.mvc.model.User;
import com.yy.springframework.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 2019/7/14.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(User user) {
        return userMapper.getUserById(user);
    }
}
