package com.yy.springframework.aop.service;

import com.yy.springframework.aop.model.Container;
import com.yy.springframework.aop.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 2019/6/7.
 */
@Component
public class UserContainer implements Container<User> {

    private ThreadLocal<List<User>> userList = new ThreadLocal<List<User>>() {
        @Override
        protected List<User> initialValue() {
            return new ArrayList<User>();
        }
    };

    public void put(User u) {
        userList.get().add(u);
    }

    public User get(int index) {
        return userList.get().get(index);
    }

    public void putAll(Collection<User> users) {
        userList.get().addAll(users);
    }
}
