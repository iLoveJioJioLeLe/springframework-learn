package com.yy.environment;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MutablePropertySources;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("dev", "test");
        context.register(AppConfig.class);
        context.refresh();
        MyDataSource myDataSource = context.getBean("myDataSource", MyDataSource.class);
        System.out.println(myDataSource);

        // 系统变量
        System.out.println(context.getEnvironment().getSystemEnvironment());
        // JVM system properties
        System.out.println(context.getEnvironment().getSystemProperties());

        MutablePropertySources sources = context.getEnvironment().getPropertySources();
        sources.addFirst(new MyPropertySource("foo"));
        System.out.println(context.getEnvironment().getProperty("bar"));
    }
}
