package com.yy.annotation.factory;

import com.yy.annotation.beans.Book;
import com.yy.annotation.beans.BookStore;
import com.yy.annotation.beans.Holiday;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/3.
 */
@Configuration
//    @Component
public class FactoryMethodComponent {

    @Bean
//    @Qualifier("aBook")
//    @Lazy
//    @Scope(value = "singleton")
    public Book myBook() {
        return new Book();
    }

    @Bean
    public BookStore myBookStore() {
        BookStore bookStore = new BookStore();
        bookStore.setBook(myBook());
        return bookStore;
    }
}
