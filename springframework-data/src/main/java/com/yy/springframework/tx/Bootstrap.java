package com.yy.springframework.tx;

import com.yy.springframework.tx.model.Product;
import com.yy.springframework.tx.model.User;
import com.yy.springframework.tx.service.ProductService;
import com.yy.springframework.tx.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

/**
 * Created by 2019/6/29.
 */
public class Bootstrap {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-service.xml");
        UserService userService = context.getBean(UserService.class);
        User user = new User(2L, "user2", "user2");
        userService.saveUserWithTx(user);
        Product product = new Product(3L, "sth", new BigDecimal(1.5));
        ProductService productService = context.getBean(ProductService.class);
        productService.saveProductWithTx(product);
        context.registerShutdownHook();
        context.close();
    }
}
