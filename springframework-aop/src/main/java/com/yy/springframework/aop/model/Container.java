package com.yy.springframework.aop.model;

import java.util.Collection;

/**
 * Created by 2019/6/7.
 */
public interface Container<T> {

    void put(T t);

    T get(int index);

    void putAll(Collection<T> collection);
}
