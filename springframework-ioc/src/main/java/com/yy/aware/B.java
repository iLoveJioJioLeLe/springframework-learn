package com.yy.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by 2019/4/14.
 */
public class B implements ApplicationContextAware{
    private A a;

    private ApplicationContext applicationContext;

    public B() {

    }

    public void testA() {
        this.a = this.applicationContext.getBean("a", A.class);
        System.out.println(a);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
