package com.yy.springframework.aop.service.impl;

import com.yy.springframework.aop.annotation.Auditable;
import com.yy.springframework.aop.annotation.Id;
import com.yy.springframework.aop.model.Product;
import com.yy.springframework.aop.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * Created by 2019/6/2.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Auditable("product.type")
    public void createProduct(@Id Product product) {
        System.out.println(product);
    }
}
