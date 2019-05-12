package com.yy.annotation;

import com.yy.annotation.beans.*;
import com.yy.annotation.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by 2019/5/3.
 */
public class JavaConfigApplication {
    public static void main(String args[]){
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.registerShutdownHook();
        BaseBean baseBean = context.getBean("baseBean", BaseBean.class);
        System.out.println(baseBean);

        Book myBook = context.getBean("myBook", Book.class);
        System.out.println(myBook);
        BookStore bookStore = context.getBean("myBookStore", BookStore.class);
        BookStore bookStore1 = context.getBean("myBookStore", BookStore.class);
        System.out.println(bookStore);
        System.out.println(bookStore1);

        UserService2 userService2 = context.getBean("us2", UserService2.class);
        System.out.println(userService2);

        UserService userService1 = context.getBean("userService1", UserService.class);
        UserService us2 = context.getBean("userService2", UserService.class);
        System.out.println(userService1);
        System.out.println(us2);
    }
}
