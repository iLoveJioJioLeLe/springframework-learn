package com.yy.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/14.
 */
public class ConfigApplication {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        context.getBean("baseConfig", BaseConfig.class);
    }
}
