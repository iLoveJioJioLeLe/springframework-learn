package com.yy.springframework.tx.service;

import com.yy.springframework.tx.model.Product;

/**
 * Created by 2019/6/29.
 */
public interface ProductService {
    void saveProductWithTx(Product product);

    void updateProductWithTx(Product product);

    Product getProductById(Long id);
}
