package com.yy.annotation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/3.
 */
@Configuration
//@Component
@ComponentScan(basePackages = {"com.yy.annotation"})
public class AppConfig {
}
