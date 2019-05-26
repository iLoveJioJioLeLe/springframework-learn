package com.yy.springframework.propertyeditor;

import com.yy.springframework.pojo.Address;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Created by 2019/5/26.
 */
//@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("postProcessBeanFactory");
        beanFactory.registerCustomEditor(Address.class, AddressEditor.class);
    }
}
