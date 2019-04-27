package com.yy.aware;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/27.
 */
public class AwareApplication {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationAware.xml");
        A a = context.getBean("a", A.class);
        System.out.println(a);
        B b = context.getBean("b", B.class);
    }
}
