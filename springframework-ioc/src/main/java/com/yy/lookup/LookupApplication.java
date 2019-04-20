package com.yy.lookup;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/14.
 */
public class LookupApplication {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("lookup.xml");
        CommandManager commandManager = context.getBean("commandManager", CommandManager.class);
        commandManager.process(1);
        commandManager.process(1);

    }
}
