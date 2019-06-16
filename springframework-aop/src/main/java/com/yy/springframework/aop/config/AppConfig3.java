package com.yy.springframework.aop.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

/**
 * Created by 2019/6/9.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableSpringConfigured
@ComponentScan("com.yy.springframework.aop")
public class AppConfig3 {
}
