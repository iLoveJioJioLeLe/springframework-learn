package com.yy.postProcessors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PostProcessorApplication {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("postProcessor.xml");
        ExampleBean1 bean1 = context.getBean("bean1", ExampleBean1.class);
        ExampleBean2 bean2 = context.getBean("bean2", ExampleBean2.class);
    }

}
