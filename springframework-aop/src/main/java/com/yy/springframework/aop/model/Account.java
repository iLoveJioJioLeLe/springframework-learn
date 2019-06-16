package com.yy.springframework.aop.model;

import com.yy.springframework.aop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.math.BigDecimal;

/**
 * Created by 2019/6/9.
 */
@Configurable
public class Account {
    @Autowired
    private UserService userService;

    private Long id;

    private BigDecimal money;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userService\":")
                .append(userService);
        sb.append(",\"id\":")
                .append(id);
        sb.append(",\"money\":")
                .append(money);
        sb.append('}');
        return sb.toString();
    }
}
