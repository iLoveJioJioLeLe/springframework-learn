package com.yy.lifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/4/21.
 */
public class MySmartLifecycleBean implements SmartLifecycle {

    private boolean isRunning = false;

    public static void main(String args[]){
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("lifecycle.xml");
        context.registerShutdownHook();
        MySmartLifecycleBean bean = context.getBean("mySmartLifecycleBean", MySmartLifecycleBean.class);
    }
    /**
     * 在单例Bean都实例化后执行
     * org.springframework.context.support.AbstractApplicationContext
     * -> refresh
     *  -> finishRefresh
     *    org.springframework.context.support.DefaultLifecycleProcessor
     *    -> onRefresh()
     *    -> startBeans(true)
     *    执行isAutoStartup=true的SmartLifecycle的start方法，按照getPhase从小到大的顺序依次执行
     * @return
     */
    public boolean isAutoStartup() {
        return true;
    }

    public void stop(Runnable callback) {
        System.out.println("MySmartLifecycleBean stop callback");
    }

    public void start() {
        System.out.println("MySmartLifecycleBean start");
        this.isRunning = true;
    }

    public void stop() {
        System.out.println("MySmartLifecycleBean stop");
        this.isRunning = false;
    }

    public boolean isRunning() {
        System.out.println("MySmartLifecycleBean isRunning is called");
        return this.isRunning;
    }

    public int getPhase() {
        System.out.println("MySmartLifecycleBean getPhase is called");
        return 0;
    }
}
