package com.yy.postProcessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Created by 2019/5/19.
 */
public class MyBeanFactoryAware implements BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        System.out.println(this.beanFactory.getBean(CustomBeanPostProcessor.class));
    }
}
