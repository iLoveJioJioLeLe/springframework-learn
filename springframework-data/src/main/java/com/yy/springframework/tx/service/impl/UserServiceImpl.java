package com.yy.springframework.tx.service.impl;

import com.yy.springframework.tx.annotation.ChangeDataSource;
import com.yy.springframework.tx.annotation.DatasourceNameEnum;
import com.yy.springframework.tx.mapper.UserMapper;
import com.yy.springframework.tx.model.User;
import com.yy.springframework.tx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by 2019/6/29.
 */
@Service
@ChangeDataSource(DatasourceNameEnum.USER)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void saveUserWithTx(User user) {
        userMapper.save(user);
    }

    @Override
    public void updateUserWithTx(User user) {
        User user1 = this.getUserById(user.getId());
        if (user1 == null) {
            throw new RuntimeException("update user error");
        }
        userMapper.update(user);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }
}
