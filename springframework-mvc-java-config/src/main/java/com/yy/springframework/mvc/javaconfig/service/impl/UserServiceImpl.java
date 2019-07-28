package com.yy.springframework.mvc.javaconfig.service.impl;

import com.yy.springframework.mvc.javaconfig.mapper.UserMapper;
import com.yy.springframework.mvc.javaconfig.model.User;
import com.yy.springframework.mvc.javaconfig.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by 2019/7/14.
 */
@Service
public class UserServiceImpl implements UserService {
//    @Autowired
    private UserMapper userMapper;

    public User getUserById(User user) {
        return userMapper.getUserById(user);
    }
}
