package com.yy.springframework.propertyeditor;

import com.yy.springframework.pojo.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/5/26.
 */
public class Bootstrap {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("propertyeditor/application.xml");
        Person bean = context.getBean("person", Person.class);
        System.out.println(bean);//{"name":"null","age":0,"address":{"province":"shanghai","city":"pd"}}
    }
}
