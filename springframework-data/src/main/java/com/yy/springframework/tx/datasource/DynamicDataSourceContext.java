package com.yy.springframework.tx.datasource;

/**
 * Created by 2019/6/29.
 */
public class DynamicDataSourceContext {
    private static final ThreadLocal<String> context = new ThreadLocal<String>();

    public static String getDataSourceKey() {
        return context.get();
    }

    public static void setDataSourceKey(String key) {
        context.set(key);
    }
}
