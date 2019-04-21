package com.yy.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by 2019/4/21.
 */
public class ExampleBean implements InitializingBean, DisposableBean {

    private String name;

    public void destroy() throws Exception {
        System.out.println("ExampleBean destroy call");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("ExampleBean afterPropertiesSet call " + this);
    }

    // 会执行 在afterPropertiesSet之后
    private void defaultInit() {
        System.out.println("ExampleBean use default init");
    }

    private void defaultDestroy() {
        System.out.println("ExampleBean use default destroy");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
