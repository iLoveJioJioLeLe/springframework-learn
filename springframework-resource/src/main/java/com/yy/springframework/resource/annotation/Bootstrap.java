package com.yy.springframework.resource.annotation;

import com.yy.springframework.resource.bean.MyBean;
import com.yy.springframework.resource.resourceloader.ResourceLoaderAware1;
import com.yy.springframework.resource.resourceloader.ResourceLoaderAware2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.UrlResource;

/**
 * Created by 2019/5/19.
 */
@ComponentScan("com.yy.springframework.resource")
public class Bootstrap {
    public static void main(String args[]){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Bootstrap.class);
        UrlResource urlResource = (UrlResource) ResourceLoaderAware1.getResource("http://www.baidu.com");
        System.out.println(urlResource);
        UrlResource urlResource2 = (UrlResource) ResourceLoaderAware2.getResource("http://www.baidu.com");
        System.out.println(urlResource2);
        UrlResource urlResource3 = (UrlResource) ResourceLoaderAware2.getResource("http://www.baidu.com");
        System.out.println(urlResource3);
        MyBean bean = ctx.getBean(MyBean.class);
        bean.printTemplate();
    }
}
