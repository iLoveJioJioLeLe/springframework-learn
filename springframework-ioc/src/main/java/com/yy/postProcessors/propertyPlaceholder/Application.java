package com.yy.postProcessors.propertyPlaceholder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/27.
 */
public class Application {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("postProcessor.xml");
        MyDataSource dataSource = context.getBean("dataSource", MyDataSource.class);
        System.out.println(dataSource);
    }
}
