package com.yy.springframework.tx;

import com.yy.springframework.tx.model.User;
import com.yy.springframework.tx.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 2019/6/29.
 */
public class Bootstrap {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-service.xml");
        UserService userService = context.getBean(UserService.class);
//        User user = new User(6L, "xxx", "user2");
//        userService.updateUserWithTx(user);
//        Product product = new Product(3L, "sth", new BigDecimal(1.5));
//        ProductService productService = context.getBean(ProductService.class);
//        productService.saveProductWithTx(product);
//        User userById = userService.getUserById(2L);
        User user = new User(6L, "xxx", "123456");
        userService.saveUserWithTx(user);
        context.registerShutdownHook();
        context.close();
    }
}
