package com.yy.environment;

import com.yy.environment.annotation.Dev;
import com.yy.environment.annotation.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Bean("myDataSource")
    @Profile("dev")
//    @Test // 自定义注解和元注解同时出现，取元注解
    public MyDataSource devDataSource() {
        MyDataSource dataSource = new MyDataSource();
        dataSource.setUrl("dev");
        dataSource.setUsername("devUser");
        dataSource.setPassword("123");
        return dataSource;
    }

    @Bean("myDataSource")
    @Profile("test")
//    @Dev
    public MyDataSource testDataSource() {
        MyDataSource dataSource = new MyDataSource();
        dataSource.setUrl("test");
        dataSource.setUsername("testUser");
        dataSource.setPassword("123");
        return dataSource;
    }
}
