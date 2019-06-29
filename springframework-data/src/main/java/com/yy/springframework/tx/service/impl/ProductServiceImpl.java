package com.yy.springframework.tx.service.impl;

import com.yy.springframework.tx.annotation.ChangeDataSource;
import com.yy.springframework.tx.annotation.DatasourceNameEnum;
import com.yy.springframework.tx.mapper.ProductMapper;
import com.yy.springframework.tx.model.Product;
import com.yy.springframework.tx.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 2019/6/29.
 */
@Service
@ChangeDataSource(DatasourceNameEnum.PRODUCT)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void saveProductWithTx(Product product) {
        productMapper.save(product);
    }

    @Override
    public void updateProductWithTx(Product product) {
        Product productById = this.getProductById(product.getId());
        if (productById == null) {
            throw new RuntimeException("updateProductWithTx error");
        }
        productMapper.update(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productMapper.getProductById(id);
    }
}
