package com.yy.springframework.aop.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by 2019/6/2.
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@ComponentScan("com.yy.springframework.aop")
public class AppConfig {
}
