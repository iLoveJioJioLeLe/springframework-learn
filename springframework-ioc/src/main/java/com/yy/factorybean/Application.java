package com.yy.factorybean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/5/1.
 */
public class Application {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("factoryBean.xml");
        MyBean bean = context.getBean("myFactoryBean", MyBean.class);
        System.out.println(bean);
        MyBean bean1 = context.getBean("myFactoryBean", MyBean.class);
        System.out.println(bean1);
        MyFactoryBean factoryBean = context.getBean("&myFactoryBean", MyFactoryBean.class);
        System.out.println(factoryBean);
    }
}
