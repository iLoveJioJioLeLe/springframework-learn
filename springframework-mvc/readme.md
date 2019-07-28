# 22.Web Mvc Framework

## 22.1 Introduction to Spring Web MVC framework

### 22.1.1 Features of Spring Web MVC

### 22.1.2 Pluggability of other MVC implementations

## 22.2 The DispatcherServlet
- Spring的web mvc框架与其他web mvc框架一样，是request-driven，通过一个中心Servlet分发请求，但是Spring的DispatcherServlet集成了SpringIOC。
- DispatcherServlet继承了HttpServlet，有两种配置方式
1. 通过web.xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-service.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-web.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>
</web-app>
```

2. Servlet3.0以后支持WebApplicationInitializer接口配置DispatcherServlet
```java
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    public void onStartup(ServletContext servletContext) throws ServletException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        ServletRegistration.Dynamic registration = servletContext.addServlet("example", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.setInitParameter("contextConfigLocation", "classpath:spring-web.xml");
        registration.addMapping("/example/*");
    }
}
```

3. javaConfig配置
```java
public class GolfingWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        // 配置root容器 service层
        return new Class<?>[] { GolfingAppConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        // 配置web子容器 controller层
        return new Class<?>[] { GolfingWebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        // DispatcherServlet映射的uri
        return new String[] { "/golfing/*" };
    }
}
```
### 22.2.1 Special Bean Types In the WebApplicationContext

- HandlerMapping
匹配request对应的handlers和a list of pre- and post-processors (handler interceptors)

- HandlerAdapter
帮助DispatcherServlet调用handler，无论实际调用哪个handler。
例如，调用带注释的控制器需要解析各种注释。

- HandlerExceptionResolver
将异常映射到视图也允许更复杂的异常处理代码。

- ViewResolver
将基于逻辑字符串的逻辑视图名称解析为实际的视图类型。

- LocaleResolver & LocaleContextResolver
解析客户端正在使用的区域设置以及可能的时区，以便能够提供国际化视图。

- ThemeResolver
解析Web应用程序可以使用的主题，例如，提供个性化布局。

- MultipartResolver
解析多部分请求，例如支持从HTML表单处理文件上载。

- FlashMapManager
存储和检索“输入”和“输出”FlashMap，可用于将属性从一个请求传递到另一个请求，通常是通过重定向。


### 22.2.2 Default DispatcherServlet Configuration
- DispatcherServlet 对上面的特殊bean有默认实现，配置在`org.springframework.web.servlet`包下的`DispatcherServlet.properties`配置中


### 22.2.3 DispatcherServlet Processing Sequence
一个请求进入DispatcherServlet处理顺序
1. request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE)获取WebApplicationContext
2. request绑定locale resolver
3. request绑定theme resolver
4. 如果request需要绑定multipart file resolver，则绑定
5. 查找合适的handler. If a handler is found, the execution chain associated with the handler (preprocessors, postprocessors, and controllers) is executed in order to prepare a model or rendering.
6. 如果有model返回，则渲染view

## 22.3 Implementing Controllers

### Advising controllers with @ControllerAdvice and @RestControllerAdvice

