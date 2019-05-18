package com.yy.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by 2019/5/18.
 */
public class MyEvent2 extends ApplicationEvent {

    public MyEvent2(Object source) {
        super(source);
    }
}
