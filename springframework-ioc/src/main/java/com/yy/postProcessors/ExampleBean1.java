package com.yy.postProcessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ExampleBean1 implements InitializingBean, DisposableBean, ApplicationContextAware {

    private String name;

    public void destroy() throws Exception {
        System.out.println("ExampleBean1 destroy");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("ExampleBean1 afterPropertiesSet");
    }

    private void init() {
        System.out.println("ExampleBean1 xml init method call");
    }

    @PostConstruct
    private void postConstruct() {
        System.out.println("@PostConstruct method call");
    }

    @PreDestroy
    private void preDestroy() {
        System.out.println("@PreDestroy method call");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ExampleBean1 applicationContextAware" + applicationContext);
    }
}
