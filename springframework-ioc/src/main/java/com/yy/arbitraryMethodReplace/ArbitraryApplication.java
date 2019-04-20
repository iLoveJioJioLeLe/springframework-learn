package com.yy.arbitraryMethodReplace;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/14.
 */
public class ArbitraryApplication {
    public static void main(String args[]){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("arbitraryApplication.xml");
        MyCalculator myCalculator = applicationContext.getBean("myCalculator", MyCalculator.class);
        String i = myCalculator.calculate("123");
    }
}
