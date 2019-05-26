package com.yy.springframework.propertyeditor;

import com.yy.springframework.pojo.Address;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 因为生命周期的原因 Bean属性注入发生在Aware接口之前，所以这里设置自定义PropertyEditor是无效的
 */
//@Component
public class MyBeanFactoryAware implements BeanFactoryAware {
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("setBeanFactory");
        ((DefaultListableBeanFactory) beanFactory).registerCustomEditor(Address.class, AddressEditor.class);
    }
}
