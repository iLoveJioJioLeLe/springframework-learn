package com.yy.springframework.aop;

import com.yy.springframework.aop.config.AppConfig3;
import com.yy.springframework.aop.model.Account;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by 2019/6/9.
 */
public class Bootstrap3 {
    public static void main(String args[]){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig3.class);
        System.out.println(new Account());
    }
}
