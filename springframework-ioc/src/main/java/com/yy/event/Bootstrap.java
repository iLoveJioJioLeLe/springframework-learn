package com.yy.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by 2019/5/18.
 */
@ComponentScan("com.yy.event")
public class Bootstrap {
    public static void main(String args[]) throws Exception{
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Bootstrap.class);
        MyPublisher myPublisher = context.getBean("myPublisher", MyPublisher.class);
        myPublisher.publishEvent(new MyEvent(myPublisher, "hello"));
        myPublisher.publishEvent(new EntityCreatedEvent(new Person()));
    }
}
