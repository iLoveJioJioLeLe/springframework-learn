package com.yy.environment;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("dev", "test");
        context.register(AppConfig.class);
        context.refresh();
        MyDataSource myDataSource = context.getBean("myDataSource", MyDataSource.class);
        System.out.println(myDataSource);
    }
}
