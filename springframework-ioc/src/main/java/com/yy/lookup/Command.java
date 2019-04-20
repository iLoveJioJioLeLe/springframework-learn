package com.yy.lookup;

/**
 * Created by 2019/4/14.
 */
public class Command {
    private Object state;
    public void setState(Object state) {
        this.state = state;
    }

    public Object execute() {
        System.out.println("state:" + state + ",execute...");
        return null;
    }
}
