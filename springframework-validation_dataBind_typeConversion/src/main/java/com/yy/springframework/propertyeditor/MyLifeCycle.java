package com.yy.springframework.propertyeditor;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/26.
 */
@Component
public class MyLifeCycle implements SmartLifecycle {

    private boolean isRunning = false;

    public boolean isAutoStartup() {
        return true;
    }

    public void stop(Runnable callback) {

    }

    public void start() {
        isRunning = true;
        System.out.println("MyLifeCycle start");
    }

    public void stop() {
        isRunning = false;
        System.out.println("MyLifeCycle sto           p");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getPhase() {
        return 0;
    }
}
