package com.yy.postProcessors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PostProcessorApplication {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("postProcessor.xml");
        // 验证init方法与BeanPostProcessor接口的顺序
        ExampleBean1 bean1 = context.getBean("bean1", ExampleBean1.class);
        // BeanPostProcessor + JDK动态代理
        ICustomBean iCustomBean = (ICustomBean) context.getBean("bean2");
        iCustomBean.printName();
        // ???
        ExampleBean1 bean3 = CustomBeanFactoryPostProcessor.getBean("bean1", ExampleBean1.class);
    }

}
