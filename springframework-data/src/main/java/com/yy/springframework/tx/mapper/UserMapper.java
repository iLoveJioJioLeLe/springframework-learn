package com.yy.springframework.tx.mapper;

import com.yy.springframework.tx.model.User;

/**
 * Created by 2019/6/29.
 */
public interface UserMapper {

    void save(User user);

    void update(User user);

    User getUserById(Long id);
}
