package com.yy.postProcessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static BeanFactory beanFactory;

    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        if (beanFactory == null) {
            beanFactory = configurableListableBeanFactory;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return beanFactory.getBean(name, clazz);
    }
}
