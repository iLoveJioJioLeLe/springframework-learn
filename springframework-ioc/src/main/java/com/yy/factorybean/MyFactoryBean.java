package com.yy.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created by 2019/5/1.
 */
public class MyFactoryBean implements FactoryBean<MyBean> {
    public MyBean getObject() throws Exception {
        MyBean bean = new MyBean();
        bean.setName(String.valueOf(System.currentTimeMillis()));
        return bean;
    }

    public Class<?> getObjectType() {
        return MyBean.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
