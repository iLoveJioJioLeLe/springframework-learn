package com.yy.annotation.beans;

import com.yy.annotation.annotations.CustomQualifier;
import com.yy.annotation.annotations.Good;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/2.
 */
@Component("bookTimeKiller")
//@Qualifier("second")
@CustomQualifier("second")
public class Book implements TimeKiller, KillerStore<Book> {
    public void print() {
        System.out.println("book");
    }
}
