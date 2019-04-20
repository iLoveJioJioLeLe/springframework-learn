package com.yy.aware;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/14.
 */
public class ApplicationAware {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationAware.xml");
        C c = context.getBean("c", C.class);
        c.getB().testA();
        c.getB().testA();
        c.getB().testA();
    }
}
