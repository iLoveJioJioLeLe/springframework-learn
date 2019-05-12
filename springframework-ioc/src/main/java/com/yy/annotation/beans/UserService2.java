package com.yy.annotation.beans;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

/**
 * Created by 2019/5/11.
 */
@Service("us2")
public class UserService2 {
    private ObjectFactory<UserDao> userDao;

    public UserService2(ObjectFactory<UserDao> userDao) {
        this.userDao = userDao;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userDao\":")
                .append(userDao);
        sb.append('}');
        return sb.toString();
    }
}
