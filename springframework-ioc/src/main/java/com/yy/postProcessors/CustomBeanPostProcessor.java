package com.yy.postProcessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CustomBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization Bean '" + beanName + "' created : " + bean.toString());
        if (bean instanceof ExampleBean1) {
            System.out.println("ExampleBean1 name before:" + ((ExampleBean1) bean).getName());
            ((ExampleBean1) bean).setName("yyy");
            System.out.println("ExampleBean1 name after:" + ((ExampleBean1) bean).getName());
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization Bean '" + beanName + "' created : " + bean.toString());
        return bean;
    }
}
