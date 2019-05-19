package com.yy.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by 2019/5/18.
 */
public class EntityCreatedEvent<T> extends ApplicationEvent {

    public EntityCreatedEvent(T entity) {
        super(entity);
    }

}
