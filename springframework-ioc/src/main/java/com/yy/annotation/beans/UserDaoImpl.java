package com.yy.annotation.beans;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/2.
 */
@Order(2)
@Component
public class UserDaoImpl implements UserDao {
}
