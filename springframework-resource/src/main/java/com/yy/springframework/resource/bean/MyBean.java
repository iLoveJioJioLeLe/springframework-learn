package com.yy.springframework.resource.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/19.
 */
@Component
public class MyBean {
    @Value("/Users/yuyue/Desktop/test.txt")
    private Resource template;

    public void printTemplate() {
        System.out.println(template);
    }
}
