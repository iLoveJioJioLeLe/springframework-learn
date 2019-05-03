package com.yy.annotation.beans;

import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

/**
 * Created by 2019/5/2.
 */
//@Order(1)
@Priority(1)
//@Primary
@Component
public class UserDaoDefaultImpl implements UserDao {
}
