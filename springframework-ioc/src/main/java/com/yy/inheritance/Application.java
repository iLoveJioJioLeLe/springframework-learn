package com.yy.inheritance;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/27.
 */
public class Application {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("inheritance.xml");
        Father father = context.getBean("father", Father.class);
        System.out.println(father);
        Son son = context.getBean("son", Son.class);
        System.out.println(son);
        Son son2 = context.getBean("son2", Son.class);
        System.out.println(son2);
    }
}
