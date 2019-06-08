package com.yy.springframework.aop.model;

import java.io.Serializable;

/**
 * Created by 2019/6/2.
 */
public class Product implements Serializable{

    private Long id;

    private String name;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"type\":")
                .append(type);
        sb.append('}');
        return sb.toString();
    }
}
