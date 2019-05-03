package com.yy.annotation;

import com.yy.annotation.beans.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/2.
 */
//@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private UserService userService;

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
