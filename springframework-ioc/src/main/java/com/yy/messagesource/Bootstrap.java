package com.yy.messagesource;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Locale;

/**
 * Created by 2019/5/18.
 */
public class Bootstrap {
    public static void main(String args[]){
        MessageSource resources = new ClassPathXmlApplicationContext("messagesource/messagesource.xml");
        String messageZhCN = resources.getMessage("message", null, "Default", null);
        System.out.println(messageZhCN);// 这是中文!
        String messageEnUS = resources.getMessage("message", null, "Default", Locale.US);
        System.out.println(messageEnUS);// Alligators rock!
        String message1 = resources.getMessage("message1", null, "im default message", null);
        System.out.println(message1);// im default message
        String message2 = resources.getMessage("argument.required", new Object[]{"UserName"}, "im default message", null);
        System.out.println(message2);// The UserName argument is required.
    }
}
