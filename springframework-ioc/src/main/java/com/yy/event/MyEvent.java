package com.yy.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by 2019/5/18.
 */
public class MyEvent extends ApplicationEvent {

    private String param;

    public MyEvent(Object source) {
        super(source);
    }

    public MyEvent(Object source, String param) {
        super(source);
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
