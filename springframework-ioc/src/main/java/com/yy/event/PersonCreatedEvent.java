package com.yy.event;

/**
 * Created by 2019/5/19.
 */
public class PersonCreatedEvent extends EntityCreatedEvent<Person> {
    public PersonCreatedEvent(Person entity) {
        super(entity);
    }
}
