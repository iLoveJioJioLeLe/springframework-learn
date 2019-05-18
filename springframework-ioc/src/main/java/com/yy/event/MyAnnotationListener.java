package com.yy.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/18.
 */
@Component
public class MyAnnotationListener {
    // 监听两个事件
    @EventListener({ContextStartedEvent.class, MyEvent.class})
    public void handleEvent(ApplicationEvent event) {
        System.out.println(event);
    }
    // 监听两个事件
    @EventListener({ContextRefreshedEvent.class, MyEvent.class})
    public void handleEvent2(ApplicationEvent event) {
        System.out.println(event);
    }

    // 监听事件，并发送另一个事件，由返回类型确定
    @EventListener({ContextRefreshedEvent.class, MyEvent.class})
    public MyEvent2 handleEvent3(ApplicationEvent event) {
        System.out.println(event);
        return new MyEvent2(this);
    }

    @EventListener({MyEvent2.class})
    public void handleEvent4(ApplicationEvent event) {
        System.out.println(event);
    }

    // 异步监听
    @EventListener({MyEvent2.class})
    @Async
    public void handleEvent5(ApplicationEvent event) {
        System.out.println(event);
    }

   /* @EventListener({ MyEvent2.class})
    public void handleEvent6(EntityCreatedEvent<MyEvent2> event) {
        System.out.println(event);
    }*/
}
