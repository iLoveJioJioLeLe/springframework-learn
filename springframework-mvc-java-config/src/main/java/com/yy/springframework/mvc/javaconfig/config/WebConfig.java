package com.yy.springframework.mvc.javaconfig.config;

import com.yy.springframework.mvc.javaconfig.interceptor.UserInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by 2019/7/27.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.yy.springframework.mvc.javaconfig.controller")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/**");
    }
}
