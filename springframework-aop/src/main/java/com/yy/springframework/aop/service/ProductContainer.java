package com.yy.springframework.aop.service;

import com.yy.springframework.aop.model.Container;
import com.yy.springframework.aop.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 2019/6/7.
 */
@Component
public class ProductContainer implements Container<Product> {

    private ThreadLocal<List<Product>> productList = new ThreadLocal<List<Product>>() {
        @Override
        protected List<Product> initialValue() {
            return new ArrayList<Product>();
        }
    };

    public void put(Product user) {
        productList.get().add(user);
    }

    public Product get(int index) {
        return productList.get().get(index);
    }

    public void putAll(Collection<Product> collection) {
        productList.get().addAll(collection);
    }
}
