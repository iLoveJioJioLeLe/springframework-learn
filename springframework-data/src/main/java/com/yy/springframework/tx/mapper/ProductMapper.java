package com.yy.springframework.tx.mapper;

import com.yy.springframework.tx.model.Product;

/**
 * Created by 2019/6/29.
 */
public interface ProductMapper {

    void save(Product product);


    void update(Product product);


    Product getProductById(Long id);
}
