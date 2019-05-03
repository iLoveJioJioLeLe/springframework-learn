package com.yy.annotation;

import com.yy.annotation.beans.Book;
import com.yy.annotation.beans.BookStore;
import com.yy.annotation.beans.Holiday;
import com.yy.annotation.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by 2019/5/3.
 */
public class JavaConfigApplication {
    public static void main(String args[]){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        BaseBean baseBean = context.getBean("baseBean", BaseBean.class);
        System.out.println(baseBean);

        Book myBook = context.getBean("myBook", Book.class);
        System.out.println(myBook);
        BookStore bookStore = context.getBean("myBookStore", BookStore.class);
        BookStore bookStore1 = context.getBean("myBookStore", BookStore.class);
        System.out.println(bookStore);
        System.out.println(bookStore1);
    }
}
