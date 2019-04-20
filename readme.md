<span id="top"></span>
[7.IOC容器](#7)
- [7.1 介绍IOC容器和Beans](#7.1)
- [7.2 容器概述](#7.2)
   - [7.2.1 配置元数据](#7.2.1)
   - [7.2.2 初始化容器](#7.2.2)
   - [7.2.3 使用容器](#7.2.3)
- [7.3 Bean概述](#7.3)
   - [7.3.1 命名Bean](#7.3.1)
   - [7.3.2 实例化Bean](#7.3.2)
- [7.4 依赖](#7.4)
   - [7.4.1 依赖注入](#7.4.1)
   - [7.4.2 依赖和配置详解](#7.4.2)
   - [7.4.3 使用depends-on](#7.4.3)
   - [7.4.4 延迟实例化Beans](#7.4.4)
   - [7.4.5 自动装配](#7.4.5)




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
[<-](#top)
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
[<-](#top)
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
     
[<-](#top)
### 7.3.1 命名Bean<span id="7.3.1"></span>
>In XML-based configuration metadata, you use the id and/or name attributes to specify the bean identifier(s). 

#### Bean别名
`<alias name="fromName" alias="toName"/>`
```
<alias name="myApp-dataSource" alias="subsystemA-dataSource"/>
<alias name="myApp-dataSource" alias="subsystemB-dataSource"/>
```
[<-](#top)
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
[<-](#top)
## 7.4 依赖<span id="7.4"></span>

[<-](#top)
### 7.4.1 依赖注入<span id="7.4.1"></span>

>Dependency injection (DI) is a process whereby objects define their dependencies, that is, the other objects they work with, only through constructor arguments, arguments to a factory method, or properties that are set on the object instance after it is constructed or returned from a factory method. The container then injects those dependencies when it creates the bean. This process is fundamentally the inverse, hence the name Inversion of Control (IoC), of the bean itself controlling the instantiation or location of its dependencies on its own by using direct construction of classes, or the Service Locator pattern.
 
>DI exists in two major variants, Constructor-based dependency injection and Setter-based dependency injection.

#### 构造方法依赖注入

```java
package examples;

public class ExampleBean {

    // Number of years to calculate the Ultimate Answer
    private int years;

    // The Answer to Life, the Universe, and Everything
    private String ultimateAnswer;

    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```
```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg type="int" value="7500000"/>
    <constructor-arg type="java.lang.String" value="42"/>
</bean>
```
```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg index="0" value="7500000"/>
    <constructor-arg index="1" value="42"/>
</bean>
```
```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg name="years" value="7500000"/>
    <constructor-arg name="ultimateAnswer" value="42"/>
</bean>
```
```java
package examples;

public class ExampleBean {

    // Fields omitted

    @ConstructorProperties({"years", "ultimateAnswer"})
    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```

#### setter依赖注入
```java
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on the MovieFinder
    private MovieFinder movieFinder;

    // a setter method so that the Spring container can inject a MovieFinder
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

### 循环依赖Circular dependencies
>If you use predominantly constructor injection, it is possible to create an unresolvable circular dependency scenario.
 For example: Class A requires an instance of class B through constructor injection, 
 and class B requires an instance of class A through constructor injection. 
 If you configure beans for classes A and B to be injected into each other,
 the Spring IoC container detects this circular reference at runtime, 
 and throws a BeanCurrentlyInCreationException.    

- 通过构造方法进行依赖注入会导致循环引用
- 通过setter方法进行依赖注入不会

[<-](#top)
### 7.4.2 依赖和配置详解<span id="7.4.2"></span>

#### 直接使用value
#### 将value转换为java.util.Properties
```xml
<bean id="baseConfig" class="com.yy.config.BaseConfig">
        <property name="properties">
            <value>
                jdbc.driver.className=com.mysql.jdbc.Driver
                jdbc.url=jdbc:mysql://localhost:3306/mysql
            </value>
        </property>
    </bean>
```
```java
public class BaseConfig {
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
```
#### idref元素
*idref和ref功能相同，但是idref能在容器启动的时候立即校验依赖是否存在*
*如果是非singleton scope类型的Bean，那么ref依赖缺少，会在实例化该Bean时报错*
```xml
<bean id="theTargetBean" class="..."/>

<bean id="theClientBean" class="...">
    <property name="targetName">
        <idref bean="theTargetBean"/>
    </property>
</bean>
```
```xml
<bean id="theTargetBean" class="..." />

<bean id="client" class="...">
    <property name="targetName" value="theTargetBean"/>
</bean>
```
#### 引用其他Beans References to other beans (collaborators)
ref元素是在<constructor-arg/> or <property/>中最后的元素。
可以指定其他Bean的id/name，用parent属性引用父容器中的Bean
```xml
<!-- in the parent context -->
<bean id="accountService" class="com.foo.SimpleAccountService">
    <!-- insert dependencies as required as here -->
</bean>
```
```xml
<!-- in the child (descendant) context -->
<bean id="accountService" <!-- bean name is the same as the parent bean -->
    class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target">
        <ref parent="accountService"/> <!-- notice how we refer to the parent bean -->
    </property>
    <!-- insert other configuration and dependencies as required here -->
</bean>
```
ref上的local属性在4.0的xsd中不支持了，可以使用ref的bean属性代替

#### 内部Beans
```xml
<bean id="outer" class="...">
    <!-- instead of using a reference to a target bean, simply define the target bean inline -->
    <property name="target">
        <bean class="com.example.Person"> <!-- this is the inner bean -->
            <property name="name" value="Fiona Apple"/>
            <property name="age" value="25"/>
        </bean>
    </property>
</bean>
```

#### 集合
```xml
<bean id="moreComplexObject" class="example.ComplexObject">
    <!-- results in a setAdminEmails(java.util.Properties) call -->
    <property name="adminEmails">
        <props>
            <prop key="administrator">administrator@example.org</prop>
            <prop key="support">support@example.org</prop>
            <prop key="development">development@example.org</prop>
        </props>
    </property>
    <!-- results in a setSomeList(java.util.List) call -->
    <property name="someList">
        <list>
            <value>a list element followed by a reference</value>
            <ref bean="myDataSource" />
        </list>
    </property>
    <!-- results in a setSomeMap(java.util.Map) call -->
    <property name="someMap">
        <map>
            <entry key="an entry" value="just some string"/>
            <entry key ="a ref" value-ref="myDataSource"/>
        </map>
    </property>
    <!-- results in a setSomeSet(java.util.Set) call -->
    <property name="someSet">
        <set>
            <value>just some string</value>
            <ref bean="myDataSource" />
        </set>
    </property>
</bean>
```
#### 集合合并 Collection merging
```xml
<beans>
    <bean id="parent" abstract="true" class="example.ComplexObject">
        <property name="adminEmails">
            <props>
                <prop key="administrator">administrator@example.com</prop>
                <prop key="support">support@example.com</prop>
            </props>
        </property>
    </bean>
    <bean id="child" parent="parent">
        <property name="adminEmails">
            <!-- the merge is specified on the child collection definition -->
            <props merge="true">
                <prop key="sales">sales@example.com</prop>
                <prop key="support">support@example.co.uk</prop>
            </props>
        </property>
    </bean>
<beans>
```
child的adminEmails最终为
```properties
administrator=administrator@example.com
sales=sales@example.com
support=support@example.co.uk
```
这种合并与list, map, set一致，不同点是list有序。

#### 集合合并的限制
*不同的集合类型不能合并，如map和list*


#### Null和空字符串values

```xml
<bean class="ExampleBean">
    <property name="email" value=""/>
</bean>
```
等同于
`exampleBean.setEmail("");`

```xml
<bean class="ExampleBean">
    <property name="email">
        <null/>
    </property>
</bean>
```
等同于
`exampleBean.setEmail(null);`

#### p命名空间和c命名空间(忽略)

#### 复合属性名称
```xml
<bean id="foo" class="foo.Bar">
    <property name="fred.bob.sammy" value="123" />
</bean>
```
foo有fred属性，fred有bob属性，bob有sammy属性，设置sammy为123.
如果foo构造结束后，有嵌套属性为null，则抛出NullPointerException.
[<-](#top)
### 7.4.3 使用depends-on <span id="7.4.3"></span>

通常我们使用setter方法进行依赖注入，在xml配置中使用<ref/>元素。
但是有时bean之间的依赖很严格，例如：一个类的静态初始化方法需要先被执行。
depends-on属性能让一个或多个bean在当前bean之前初始化。
```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager"/>
<bean id="manager" class="ManagerBean" />
```
或配置多个依赖bean，用逗号或空格或分号分割
```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager,accountDao">
    <property name="manager" ref="manager" />
</bean>

<bean id="manager" class="ManagerBean" />
<bean id="accountDao" class="x.y.jdbc.JdbcAccountDao" />
```
[<-](#top)
### 7.4.4 延迟实例化Beans <span id="7.4.4"></span>
>By default, ApplicationContext implementations eagerly create and configure all singleton beans as part of the initialization process. Generally, this pre-instantiation is desirable, because errors in the configuration or surrounding environment are discovered immediately, as opposed to hours or even days later. When this behavior is not desirable, you can prevent pre-instantiation of a singleton bean by marking the bean definition as lazy-initialized. A lazy-initialized bean tells the IoC container to create a bean instance when it is first requested, rather than at startup.

```xml
<bean id="lazy" class="com.foo.ExpensiveToCreateBean" lazy-init="true"/>
<bean name="not.lazy" class="com.foo.AnotherBean"/>
```
*但是如果一个lazy-initialized bean是一个singleton Bean的依赖项时，它会在ApplicationContext启动时创建*

配置容器级别延迟实例化
```xml
<beans default-lazy-init="true">
    <!-- no beans will be pre-instantiated... -->
</beans>
```
[<-](#top)
### 7.4.5 自动装配 <span id="7.4.5"></span>
```xml
<bean id="xxx" class="xxx" autowire="byType"></bean>
```
| Mode | Explanation |
| ------ | ------ |
| no     | 不使用自动装配 |
| byName | 通过名称自动装配 |
| byType | 通过类型自动装配，如果容器中存在类型相同的Bean则注入，否则为Null |
| byName | 与byType类似，但是用于构造方法的入参，如果容器里没有该类型的Bean会抛出异常 |


