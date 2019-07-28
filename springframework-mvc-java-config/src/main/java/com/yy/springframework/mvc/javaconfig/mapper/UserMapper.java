package com.yy.springframework.mvc.javaconfig.mapper;

import com.yy.springframework.mvc.javaconfig.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    User getUserById(User user);
}
