###[7.IOC容器](#7)
- [7.1 介绍IOC容器和Beans](#7.1)
- [7.2 容器概述](#7.2)
   - [7.2.1 配置元数据](#7.2.1)
   - [7.2.2 初始化容器](#7.2.2)
   - [7.2.3 使用容器](#7.2.3)
- [7.3 Bean概述](#7.3)
   - [7.3.1 命名Bean](#7.3.1)
   - [7.3.2 实例化Bean](#7.3.2)
   

# 7. IOC容器<span id="7"></span>

## 7.1 介绍IOC容器和Beans<span id="7.1"></span>

## 7.2 容器概述<span id="7.2"></span>
>IoC is also known as dependency injection (DI). It is a process whereby objects define their dependencies, that is, the other objects they work with, only through constructor arguments, arguments to a factory method, or properties that are set on the object instance after it is constructed or returned from a factory method. The container then injects those dependencies when it creates the bean. This process is fundamentally the inverse, hence the name Inversion of Control (IoC), of the bean itself controlling the instantiation or location of its dependencies by using direct construction of classes, or a mechanism such as the Service Locator pattern.
### 7.2.1 配置元数据<span id="7.2.1"></span>
>configuration metadata represents how you as an application developer tell the Spring container to instantiate, configure, and assemble the objects in your application.
For information about using other forms of metadata with the Spring container, see:
1. Annotation-based configuration: Spring 2.5 introduced support for annotation-based configuration metadata.
2. Java-based configuration: Starting with Spring 3.0, many features provided by the Spring JavaConfig project became part of the core Spring Framework. Thus you can define beans external to your application classes by using Java rather than XML files. To use these new features, see the @Configuration, @Bean, @Import and @DependsOn annotations.

### 7.2.2 初始化容器<span id="7.2.2"></span>
`ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="" class="">
    </bean>
</beans>
```

### 7.2.3 使用容器<span id="7.2.3"></span>
>The ApplicationContext is the interface for an advanced factory capable of maintaining a registry of different beans and their dependencies. Using the method T getBean(String name, Class<T> requiredType) you can retrieve instances of your beans.
```java
// create and configure beans
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

// retrieve configured instance
PetStoreService service = context.getBean("petStore", PetStoreService.class);

// use configured instance
List<String> userList = service.getUsernameList();
```

## 7.3 Bean概述<span id="7.3"></span>

### Bean定义
- Property
   - class
   - name
   - scope
   - constructor arguments
   - properties
   - autowiring mode
   - lazy-initialization mode
   - initialization method
   - destruction method
     
### 7.3.1 命名Bean<span id="7.3.1"></span>
>In XML-based configuration metadata, you use the id and/or name attributes to specify the bean identifier(s). 

#### Bean别名
`<alias name="fromName" alias="toName"/>`
```
<alias name="myApp-dataSource" alias="subsystemA-dataSource"/>
<alias name="myApp-dataSource" alias="subsystemB-dataSource"/>
```

### 7.3.2 实例化Bean<span id="7.3.2"></span>
- 静态内部类配置Bean
>Inner class names.  If you want to configure a bean definition for a static nested class, you have to use the binary name of the nested class.

>For example, if you have a class called Foo in the com.example package, and this Foo class has a static nested class called Bar, the value of the 'class' attribute on a bean definition would be…​
com.example.Foo$Bar

>Notice the use of the $ character in the name to separate the nested class name from the outer class name.
- 通过构造方法
```java
<bean id="exampleBean" class="examples.ExampleBean"/>

<bean name="anotherExample" class="examples.ExampleBeanTwo"/>
```
- 通过静态工厂方法
```
<bean id="clientService"
    class="examples.ClientService"
    factory-method="createInstance"/>
```
```java
public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }
}
```
- 通过实例工厂方法
```java
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject any dependencies required by this locator bean -->
</bean>

<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>

<bean id="accountService"
    factory-bean="serviceLocator"
    factory-method="createAccountServiceInstance"/>
```
```java
public class DefaultServiceLocator {

    private static ClientService clientService = new ClientServiceImpl();

    private static AccountService accountService = new AccountServiceImpl();

    public ClientService createClientServiceInstance() {
        return clientService;
    }

    public AccountService createAccountServiceInstance() {
        return accountService;
    }
}
```




