package com.yy.postProcessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CustomBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization Bean '" + beanName + "' created : " + bean.toString());
        // ExampleBean1修改name属性
        if (bean instanceof ExampleBean1) {
            System.out.println("ExampleBean1 name before:" + ((ExampleBean1) bean).getName());
            ((ExampleBean1) bean).setName("yyy");
            System.out.println("ExampleBean1 name after:" + ((ExampleBean1) bean).getName());
        }
        return bean;
    }

    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization Bean " + beanName);
        // bean2使用JDK动态代理
        if (beanName.equals("bean2")) {
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals("printName")) {
                        System.out.println("代理printName");
                    }
                    return method.invoke(bean, args);
                }
            });
        }
        return bean;
    }
}
