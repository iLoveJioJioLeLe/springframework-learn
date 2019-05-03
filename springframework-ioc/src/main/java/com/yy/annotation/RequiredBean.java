package com.yy.annotation;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/2.
 */
@Component
public class RequiredBean {

    @Value("1221")
    private String name;

    public String getName() {
        return name;
    }

    @Required
    @Value("111")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"name\":\"")
                .append(name).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
