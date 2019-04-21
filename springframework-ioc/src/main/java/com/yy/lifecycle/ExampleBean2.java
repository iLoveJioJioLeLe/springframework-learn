package com.yy.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by 2019/4/21.
 */

public class ExampleBean2 {

    @PostConstruct
    private void init() {
        System.out.println("ExampleBean2 init " + this);

    }

    @PreDestroy
    private void destroy() {
        System.out.println("ExampleBean2 destroy");
    }

    // 会执行 在init之后
    private void defaultInit() {
        System.out.println("ExampleBean2 use default init");
    }

    private void defaultDestroy() {
        System.out.println("ExampleBean2 use default destroy");
    }

}
