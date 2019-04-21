package com.yy.lifecycle;

/**
 * Created by 2019/4/21.
 */
public class ExampleBean3 {
    private String name;

    private void init() {
        System.out.println("ExampleBean3 init " + this);
    }

    private void destroy() {
        System.out.println("ExampleBean3 destroy");
    }

    // 不会执行
    private void defaultInit() {
        System.out.println("ExampleBean3 use default init");
    }

    private void defaultDestroy() {
        System.out.println("ExampleBean3 use default destroy");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
