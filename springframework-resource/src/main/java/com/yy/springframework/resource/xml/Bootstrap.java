package com.yy.springframework.resource.xml;

import com.yy.springframework.resource.bean.MyBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/5/19.
 */
public class Bootstrap {
    public static void main(String args[]){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"service.xml"}, Bootstrap.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.println(bean);
    }
}
