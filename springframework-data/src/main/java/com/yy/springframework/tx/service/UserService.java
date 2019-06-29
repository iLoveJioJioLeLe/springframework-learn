package com.yy.springframework.tx.service;

import com.yy.springframework.tx.model.User;

/**
 * Created by 2019/6/29.
 */
public interface UserService {

    void saveUserWithTx(User user);

    void updateUserWithTx(User user);

    User getUserById(Long id);
}
