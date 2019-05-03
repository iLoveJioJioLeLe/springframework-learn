package com.yy.annotation.beans;

import com.yy.aware.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Created by 2019/5/2.
 */
@Component
public class UserService {
    private UserDao userDao;

    private UserDao[] userDaos;

    @Autowired(required = false)
    private A a;

    @Autowired
    private Map<String, UserDao> userDaoMap;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    @Autowired
    public UserService(UserDao[] userDaos) {
        this.userDaos = userDaos;
    }
    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userDao\":")
                .append(userDao);
        sb.append(",\"userDaos\":")
                .append(Arrays.toString(userDaos));
        sb.append(",\"userDaoMap\":")
                .append(userDaoMap);
        sb.append('}');
        return sb.toString();
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}
