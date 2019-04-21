package com.yy.scope;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 2019/4/20.
 */
public class ScopeApplication {

    private static ApplicationContext context = new ClassPathXmlApplicationContext("scope.xml");

    public static void main(String args[]) throws Exception{
        Thread t1 = new Thread(new Task());
        Thread t2 = new Thread(new Task());
        t1.start();
        t2.start();
    }

    static class Task implements Runnable {

        public void run() {
            Foo foo = context.getBean("foo", Foo.class);
            System.out.println(foo);
            Foo foo2 = context.getBean("foo", Foo.class);
            System.out.println(foo2);
        }
    }
}
