package com.yy.annotation;

import com.yy.annotation.beans.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/5/1.
 */
public class Application {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("annotation/annotation.xml");
        BaseBean baseBean = context.getBean("baseBean", BaseBean.class);
        // annotation在xml前注入
        System.out.println(baseBean);

        RequiredBean requiredBean = context.getBean("requiredBean", RequiredBean.class);
        System.out.println(requiredBean);

        UserService userService = context.getBean("userService", UserService.class);
        System.out.println(userService);

        Holiday holiday = context.getBean("holiday", Holiday.class);
        System.out.println(holiday);
        Book book = (Book)holiday.getKillerStore();
        book.print();

        Weekend weekend = context.getBean("weekend", Weekend.class);
        System.out.println(weekend);
    }
}
