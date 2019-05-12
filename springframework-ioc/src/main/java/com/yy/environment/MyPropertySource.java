package com.yy.environment;

import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 2019/5/12.
 */
public class MyPropertySource extends PropertySource {

    private static final Map<String, String> properties = new HashMap<String, String>();

    static {
        properties.put("foo", "hello foo");
        properties.put("bar", "hello bar");
        properties.put("customer", "config");
    }
    public MyPropertySource(String name) {
        super(name);
    }
    public MyPropertySource(String name, Object source) {
        super(name, source);
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }
}
