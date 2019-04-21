package com.yy.lifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/21.
 */
public class LifecycleApplication {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("lifecycle.xml");
        ExampleBean exampleBean = context.getBean("exampleBean", ExampleBean.class);
        ExampleBean2 exampleBean2 = context.getBean("exampleBean2", ExampleBean2.class);
        ExampleBean3 exampleBean3 = context.getBean("exampleBean3", ExampleBean3.class);
        ExampleBean4 exampleBean4 = context.getBean("exampleBean4", ExampleBean4.class);

    }
}
