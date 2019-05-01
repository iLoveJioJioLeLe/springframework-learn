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
   - [7.4.6 方法注入](#7.4.6)
- [7.5 Bean作用域](#7.5)
   - [7.5.1 单例singleton作用域](#7.5.1)
   - [7.5.2 原型prototype作用域](#7.5.2)
   - [7.5.3 单例Bean依赖原型Bean](#7.5.3)
   - [7.5.4 Request,Session,Global Session,Application,WebSocket Scopes](#7.5.4)
   - [7.5.5 自定义作用域](#7.5.5)
- [7.6 自定义Bean特性](#7.6)
   - [7.6.1 LifeCycle回调](#7.6.1)
   - [7.6.2 ApplicationContextAware和BeanNameAware](#7.6.2)
   - [7.6.3 其他Aware接口](#7.6.3)
- [7.7 Bean定义继承](#7.7)
- 
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

[<-](#top)
### 7.4.6 方法注入 <span id="7.4.6"></span>

#### 问题：A单例依赖一个非单例B

#### 解决方案1：A实现ApplicationContextAware直接操作容器
```java
// a class that uses a stateful Command-style class to perform some processing
package fiona.apple;

// Spring-API imports
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CommandManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public Object process(Map commandState) {
        // grab a new instance of the appropriate Command
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    protected Command createCommand() {
        // notice the Spring API dependency!
        return this.applicationContext.getBean("command", Command.class);
    }

    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
```
#### 解决方案2：Lookup方法注入
>Lookup method injection is the ability of the container to override methods on container managed beans, to return the lookup result for another named bean in the container. The lookup typically involves a prototype bean as in the scenario described in the preceding section. The Spring Framework implements this method injection by using bytecode generation from the CGLIB library to generate dynamically a subclass that overrides the method.

Spring容器能够通过Lookup方法注入重写容器中Bean的方法。使用动态生成字节码CGLIB自动生成子类重写方法。
1. 方法不能是final类型
2. 单元测试需要实例方法
3. 使用component-scan则需要实例方法
4. @Bean方法不支持
```java
package fiona.apple;

// no more Spring imports!

public abstract class CommandManager {

    public Object process(Object commandState) {
        // grab a new instance of the appropriate Command interface
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    // okay... but where is the implementation of this method?
    protected abstract Command createCommand();
}
```
```xml
<!-- a stateful bean deployed as a prototype (non-singleton) -->
<bean id="myCommand" class="fiona.apple.AsyncCommand" scope="prototype">
    <!-- inject dependencies here as required -->
</bean>

<!-- commandProcessor uses statefulCommandHelper -->
<bean id="commandManager" class="fiona.apple.CommandManager">
    <lookup-method name="createCommand" bean="myCommand"/>
</bean>
```
方法注入需要方法签名如下：
`<public|protected> [abstract] <return-type> theMethodName(no-arguments);`
如果方法是抽象的，自动生成的子类会实现这个方法。
其他情况，自动生成的子类会重写原来的实例方法。

#### 使用Lookup注解
1. 通过Bean名字注入
```java
public abstract class CommandManager {

    public Object process(Object commandState) {
        Command command = createCommand();
        command.setState(commandState);
        return command.execute();
    }

    @Lookup("myCommand")
    protected abstract Command createCommand();
}
```
2. 通过类型匹配注入
```java
public abstract class CommandManager {

    public Object process(Object commandState) {
        MyCommand command = createCommand();
        command.setState(commandState);
        return command.execute();
    }

    @Lookup
    protected abstract MyCommand createCommand();
}
```

#### 解决方案3：方法替换Arbitrary method replacement
```java
public class MyCalculatorMethodReplacer implements MethodReplacer {
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        return args[0] + " from replacer";
    }
}
```
```java
public class MyCalculator {
    public String calculate(String input) {
        return input;
    }
}
```
```xml
    <bean class="com.yy.arbitraryMethodReplace.MyCalculatorMethodReplacer" id="myCalculatorMethodReplacer"/>
    <bean class="com.yy.arbitraryMethodReplace.MyCalculator" id="myCalculator">
        <replaced-method name="calculate" replacer="myCalculatorMethodReplacer">
            <arg-type>String</arg-type>
        </replaced-method>
    </bean>
```

[<-](#top)
## 7.5 Bean作用域<span id="7.5"></span>

| Scope | Description |
| - | - |
| singleton | 默认。一个SpringIOC容器一个单例对象 |
| prototype | 任意数量对象实例 |
| request | 一个Http请求一个对象 |
| session | 一个HttpSession一个对象 |
| globalSession | 一个全局HttpSession一个对象，仅在portlet上下文存在 |
| application | 一个ServletContext一个对象 |
| websocket | 一个WebSocket一个对象 |

在Spring3.0中支持thread scope(SimpleThreadScope)

[<-](#top)
### 7.5.1 单例singleton作用域<span id="7.5.1"></span>
```xml
<bean id="accountService" class="com.foo.DefaultAccountService"/>

<!-- the following is equivalent, though redundant (singleton scope is the default) -->
<bean id="accountService" class="com.foo.DefaultAccountService" scope="singleton"/>
```

[<-](#top)
### 7.5.2 原型prototype作用域<span id="7.5.2"></span>
```xml
<bean id="accountService" class="com.foo.DefaultAccountService" scope="prototype"/>
```
>In contrast to the other scopes, Spring does not manage the complete lifecycle of a prototype bean: the container instantiates, configures, and otherwise assembles a prototype object, and hands it to the client, with no further record of that prototype instance. Thus, although initialization lifecycle callback methods are called on all objects regardless of scope, in the case of prototypes, configured destruction lifecycle callbacks are not called. The client code must clean up prototype-scoped objects and release expensive resources that the prototype bean(s) are holding. To get the Spring container to release resources held by prototype-scoped beans, try using a custom bean post-processor, which holds a reference to beans that need to be cleaned up.
 
 Spring不负责管理原型对象的完整生命周期。  
 原型对象实例化后就由客户端管理，
 尽管初始化生命周期回调方法会被执行，
 但是销毁生命周期回调方法不会被执行。
 通过使用自定义的BeanPostProcessor可以获取需要清理的原型对象的引用。
 
[<-](#top)
### 7.5.3 单例Bean依赖原型Bean<span id="7.5.3"></span>
[参照7.4.6 方法注入](#7.4.6)


[<-](#top)
### 7.5.4 Request,Session,Global Session,Application,WebSocket Scopes<span id="7.5.4"></span>
>The request, session, globalSession, application, and websocket scopes are only available if you use a web-aware Spring ApplicationContext implementation (such as XmlWebApplicationContext). If you use these scopes with regular Spring IoC containers such as the ClassPathXmlApplicationContext, an IllegalStateException will be thrown complaining about an unknown bean scope.
>The Spring IoC container manages not only the instantiation of your objects (beans), but also the wiring up of collaborators (or dependencies). If you want to inject (for example) an HTTP request scoped bean into another bean of a longer-lived scope, you may choose to inject an AOP proxy in place of the scoped bean. That is, you need to inject a proxy object that exposes the same public interface as the scoped object but that can also retrieve the real target object from the relevant scope (such as an HTTP request) and delegate method calls onto the real object.

SpringIOC容器可以通过注入AOP代理的方式，
让一个短生命周期的Bean（如request）注入到一个长生命周期的Bean（如singleton）中
```xml
    <!-- an HTTP Session-scoped bean exposed as a proxy -->
    <bean id="userPreferences" class="com.foo.UserPreferences" scope="session">
        <!-- instructs the container to proxy the surrounding bean -->
        <aop:scoped-proxy/>
    </bean>

    <!-- a singleton-scoped bean injected with a proxy to the above bean -->
    <bean id="userService" class="com.foo.SimpleUserService">
        <!-- a reference to the proxied userPreferences bean -->
        <property name="userPreferences" ref="userPreferences"/>
    </bean>
```
#### 对FactoryBean的实现类配置aop:scoped-proxy
返回的是FactoryBean实现类本身，作用域也是FactoryBean本身，而不是工厂Bean的getObject获取的对象。

#### 选择代理类型
默认是CGLIB代理，如果想使用JDK代理，配置如下
```xml
<!-- DefaultUserPreferences implements the UserPreferences interface -->
<bean id="userPreferences" class="com.foo.DefaultUserPreferences" scope="session">
    <aop:scoped-proxy proxy-target-class="false"/>
</bean>

<bean id="userManager" class="com.foo.UserManager">
    <property name="userPreferences" ref="userPreferences"/>
</bean>
```

[<-](#top)
### 7.5.5 自定义作用域<span id="7.5.5"></span>
  Bean作用域技术是可扩展的，能够自定义作用域，甚至重新定义已经存在的作用域。
但是不能覆盖内置的单例和原型范围。  
  通过实现org.springframework.beans.factory.config.Scope接口自定义作用域。
  Scope接口有四个方法：
  1. 获取对象
    `Object get(String name, ObjectFactory objectFactory)`
  2. 从作用域移除对象，返回该对象
    `Object remove(String name)`
  3. 注册作用域对象销毁后的回调方法
   `void registerDestructionCallback(String name, Runnable destructionCallback)`
  4. 获取作用域标识
    `String getConversationId()`

#### 使用自定义作用域
1. 方法1：使用ConfigurableBeanFactory接口的
`void registerScope(String scopeName, Scope scope);`方法
```java
Scope threadScope = new SimpleThreadScope();
beanFactory.registerScope("thread", threadScope);
```
2. 方法2：使用xml配置
```xml
<bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
        <property name="scopes">
            <map>
                <entry key="custom">
                    <bean class="com.yy.scope.CustomScope"/>
                </entry>
            </map>
        </property>
    </bean>

    <bean id="bar" class="com.yy.scope.Bar" scope="custom">
        <aop:scoped-proxy/>
    </bean>

    <bean id="foo" class="com.yy.scope.Foo">
        <property name="bar" ref="bar"/>
    </bean>
```
[<-](#top)
## 7.6 自定义Bean性质<span id="7.6"></span>


### 7.6.1 Lifecycle回调<span id="7.6.1"></span>

实现Bean实现InitializingBean和DisposableBean接口，
Spring容器会在Bean初始化时调用afterPropertiesSet()方法，
在Bean销毁时调用destroy()方法。

另外Spring还提供了BeanPostProcessor接口处理回调[7.8](#7.8)。
本章还介绍了LifeCycle接口让对象参与容器的启动和关闭。

注：也可使用JSR-250@PostConstruct和@PreDestroy注解，让Bean不与Spring耦合。

1. 实现InitializingBean和DisposableBean接口
```java
public class ExampleBean implements InitializingBean, DisposableBean {

    private String name;

    public void destroy() throws Exception {
        System.out.println("ExampleBean destroy call");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("ExampleBean afterPropertiesSet call " + this);
    }
}
```
```xml
    <bean id="exampleBean" class="com.yy.lifecycle.ExampleBean">
       <property name="name" value="foo"/>
   </bean>
```

2. 使用JSR-250@PostConstruct和@PreDestroy注解
```java
public class ExampleBean2 {

    @PostConstruct
    private void init() {
        System.out.println("ExampleBean2 init " + this);

    }

    @PreDestroy
    private void destroy() {
        System.out.println("ExampleBean2 destroy");
    }
}
```
```xml
    <context:component-scan base-package="com.yy.lifecycle"/>
    <bean id="exampleBean2" class="com.yy.lifecycle.ExampleBean2"/>
```

3. xml配置bean属性init-method和destroy-method
```java
public class ExampleBean3 {

    private void init() {
        System.out.println("ExampleBean3 init " + this);
    }

    private void destroy() {
        System.out.println("ExampleBean3 destroy");
    }
}
```
```xml
    <bean id="exampleBean3" class="com.yy.lifecycle.ExampleBean3" init-method="init" destroy-method="destroy">

    </bean>
```

4. 在beans上配置default-init-method和default-destroy-method
```java
public class ExampleBean4 {

    private void defaultInit() {
        System.out.println("ExampleBean4 use default init");
    }

    private void defaultDestroy() {
        System.out.println("ExampleBean4 use default destroy");
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd"
       default-init-method="defaultInit" default-destroy-method="defaultDestroy"
>
    <bean id="exampleBean4" class="com.yy.lifecycle.ExampleBean4">
    </bean>
</beans>
```
>The Spring container guarantees that a configured initialization callback is called immediately after a bean is supplied with all dependencies. Thus the initialization callback is called on the raw bean reference, which means that AOP interceptors and so forth are not yet applied to the bean. A target bean is fully created first, then an AOP proxy (for example) with its interceptor chain is applied. If the target bean and the proxy are defined separately, your code can even interact with the raw target bean, bypassing the proxy. Hence, it would be inconsistent to apply the interceptors to the init method, because doing so would couple the lifecycle of the target bean with its proxy/interceptors and leave strange semantics when your code interacts directly to the raw target bean.

Spring容器保证为Bean提供所有依赖后立即调用配置的初始化回调方法。
意味着AOP拦截器尚未应用于Bean，初始化回调方法直接作用于Bean。

#### 组合lifecycle技术
同时使用多种lifecycle回调方法
1. 方法名相同时，回调方法只会执行一次。
2. 方法名不同时，回调方法按照下面的顺序执行。
- 初始化方法
   - @PostConstruct方法
   - InitializingBean的afterPropertiesSet方法
   - 自定义xml配置的init方法
- 销毁方法
   - @PreDestroy方法
   - DisposableBean的destroy方法
   - 自定义xml配置的destroy方法


#### 启动和关闭回调方法

LifeCycle接口为有自己生命周期需求的对象定义了必要的方法。

```java
public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();
}
```
>Any Spring-managed object may implement that interface. Then, when the ApplicationContext itself receives start and stop signals, e.g. for a stop/restart scenario at runtime, it will cascade those calls to all Lifecycle implementations defined within that context. It does this by delegating to a LifecycleProcessor
```java
public interface LifecycleProcessor extends Lifecycle {

    void onRefresh();

    void onClose();
}
```
每个Spring管理的对象都可以实现Lifecycle接口，当ApplicationContext收到启动/关闭信号时，
它会调用所有当前容器中Lifecycle实例的对应方法，通过LifecycleProcessor代理。

*注意：Lifecycle接口只是显示启动/停止通知的简单合约，并不意味着在上下文刷新时自动调用它的start方法。

#### 实现org.springframework.context.SmartLifecycle接口

实现SmartLifecycle接口能够对bean自动启动进行细粒度控制，包括启动阶段。
*注意：在Bean销毁前不能保证stop调用。在常规关闭时，所有Lifecycle Bean将在传播一般destroy回调前首先收到停止通知；
但是在上下文生命周期中的热刷新或中止刷新尝试时，只会调用destroy方法。

虽然通过depends-on能控制bean之间的初始化顺序，但是有时候这种直接依赖关系是未知的。
所以SmartLifecycle接口继承了Phased接口，通过Phased接口的getPhase()方法确定Lifecycle接口的start方法顺序。
getPhase返回的值越小，启动(start)越早，关闭(stop)越晚。
实现LifeCycle接口的实例默认getPhase返回0。


LifecycleProcessor调用容器里的所有Lifecycle的stop(Runnable run)回调方法时，默认每个阶段(根据getPhase分组)超时时间30秒。
可以通过在容器中定义名为lifecycleProcessor的Bean来覆盖DefaultLifecycleProcessor，并修改超时时间。
```xml
<bean id="lifecycleProcessor" class="org.springframework.context.support.DefaultLifecycleProcessor">
    <!-- timeout value in milliseconds -->
    <property name="timeoutPerShutdownPhase" value="10000"/>
</bean>
```

在单例Bean都实例化后执行
 org.springframework.context.support.AbstractApplicationContext
  -> refresh
   -> finishRefresh
    org.springframework.context.support.DefaultLifecycleProcessor
     -> onRefresh()
      -> startBeans(true)
      执行isAutoStartup=true的SmartLifecycle的start方法，按照getPhase从小到大的顺序依次执行


#### 优雅关闭非web应用的SpringIOC容器

Spring基于Web的ApplicationContext已经是优雅关闭SpringIOC容器了。

非Web的ApplicationContext需要注册shutdown hook
```java
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Boot {

    public static void main(final String[] args) throws Exception {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        // add a shutdown hook for the above context...
        ctx.registerShutdownHook();

        // app runs here...

        // main method exits, hook is called prior to the app shutting down...
    }
}
```

[<-](#top)
### 7.6.2 ApplicationContextAware和BeanNameAware<span id="7.6.2"></span>

ApplicationContext为ApplicationContextAware接口的实现类提供了当前ApplicationContext的引用。
也可以通过constructor和byType自动装配applicationContext依赖。
```java
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```
ApplicationContext为BeanNameAware接口的实现类提供了当前接口实现类在IOC容器内的name引用。
这个回调方法在所有bean的普通属性注入后执行，
在初始化方法执行前执行。
```java
public interface BeanNameAware {

    void setBeanName(String name) throws BeansException;
}
```
BeanFactoryAware-
普通的bean属性注入-
BeanNameAware-
ApplicationContextAware-
自定义BeanPostProcessor(before)-
init方法-
自定义BeanPostProcessor(after)

[<-](#top)
### 7.6.3 其他Aware接口<span id="7.6.3"></span>

| Name | InjectDependency | Explain in... |
| - | - | - |
| ApplicationContextAware | Declaring ApplicationContext | [7.6.2](#7.6.2) |
| ApplicationEventPublisherAware | Event publisher of the enclosing ApplicationContext | [7.15](#7.15) |
| BeanClassLoaderAware | Class loader used to load the bean classes. | [7.3.2](#7.3.2) |
| BeanFactoryAware | Declaring BeanFactory | [7.6.2](#7.6.2) |
| BeanNameAware | Name of the declaring bean | [7.6.2](#7.6.2) |
| BootstrapContextAware | Resource adapter BootstrapContext the container runs in. Typically available only in JCA aware ApplicationContexts | [32](#32) |
| LoadTimeWeaverAware | Defined weaver for processing class definition at load time | [11.8.4](#11.8.4) |
| MessageSourceAware | Configured strategy for resolving messages (with support for parametrization and internationalization) | [7.15](#7.15) |
| NotificationPublisherAware | Spring JMX notification publisher | [31.7](#31.7) |
| PortletConfigAware | Current PortletConfig the container runs in. Valid only in a web-aware Spring ApplicationContext | [25](#25) |
| PortletContextAware | Current PortletContext the container runs in. Valid only in a web-aware Spring ApplicationContext | [25](#25) |
| ResourceLoaderAware | Configured loader for low-level access to resources | [8](#8) |
| ServletConfigAware | Current ServletConfig the container runs in. Valid only in a web-aware Spring ApplicationContext | [22](#22) |
| ServletContextAware | Current ServletContext the container runs in. Valid only in a web-aware Spring ApplicationContext | [22](#22) |

                        
## 7.7 Bean定义继承<span id="7.7"></span>
>If you work with an ApplicationContext interface programmatically, child bean definitions are represented by the ChildBeanDefinition class. Most users do not work with them on this level, instead configuring bean definitions declaratively in something like the ClassPathXmlApplicationContext. When you use XML-based configuration metadata, you indicate a child bean definition by using the parent attribute, specifying the parent bean as the value of this attribute.

```xml
    <bean id="father" class="com.yy.inheritance.Father">
        <property name="name" value="foo"/>
        <property name="age" value="100"/>
    </bean>
    <bean id="son" class="com.yy.inheritance.Son" parent="father">
        <!-- name会继承father -->
        <property name="age" value="20"/>
        <property name="email" value="spring.io"/>
    </bean>
```
属性abstract="true"的bean是不能实例化的，只能作为一个模板bean。
```xml
    <bean id="father2" abstract="true">
        <property name="name" value="bar"/>
        <property name="age" value="99"/>
    </bean>
    <bean id="son2" class="com.yy.inheritance.Son" parent="father2">
        <property name="age" value="19"/>
        <property name="email" value="springframework"/>
    </bean>
```
child bean会继承的有
- scope
- constructor argument values
- property values
- method overrides from parent

特别的：Any scope, initialization method, destroy method, and/or static factory method settings that you specify will override the corresponding parent settings.
父亲的init方法，子类必须重写，不然报错。
`BeanDefinitionValidationException: Couldn't find an init method named 'hello' on bean with name 'son'`
不会继承的有
- depends on
- autowire mode
- dependency check
- singleton
- lazy init

[<-](#top)
## 7.8 容器扩展点Container Extension Points<span id="7.8"></span>
                   
### 7.8.1 使用BeanPostProcessor自定义Bean<span id="7.8.1"></span>
```java
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
```
- BeanPostProcessor接口定义了您可以实现的回调方法，以提供您自己的（或覆盖容器的默认）实例化逻辑，依赖关系解析逻辑等。
- 可以配置多个BeanPostProcessor实例，通过实现Ordered接口来设置顺序。
- BeanPostProcessor的作用域是单个容器。                   
- 对于容器中的每个Bean，BeanPostProcessor会在Bean的initialization methods (such as InitializingBean’s afterPropertiesSet() or any declared init method)
  前执行postProcessBeforeInitialization方法，
  后执行postProcessAfterInitialization方法。
- BeanPostProcessor通常会检查回调接口，或者可以使用代理包装Bean。一些SpringAOP基础结构类实现为Bean后处理器，以便提供代理包装逻辑。
- ApplicationContext自动检测定义在配置元数据中实现BeanPostProcessor接口的实例。


[<-](#top) 
### 7.8.2 通过BeanFactoryPostProcessor自定义配置元数据<span id="7.8.2"></span>
BeanFactoryPostProcessor与BeanPostProcessor的主要区别是：
BeanFactoryPostProcessor在bean的配置元数据上操作，也就是说SpringIOC容器允许它读取配置元数据，
并且可能在容器实例化任何bean之前，改变配置元数据（除了BeanFactoryPostProcessor以外）。
- 可以配置多个BeanFactoryPostProcessor，通过实现Ordered接口设置顺序
- 如果在BeanFactoryPostProcessor接口实现类中，使用BeanFactory的getBean方法，会导致Bean提前实例化，引起Bean绕过后处理过程。如：BeanPostProcessor
  （测试发现@PostConstruct也绕过了，只执行了xml配置的init方法和InitializingBean的初始化方法）
- BeanFactoryPostProcessor的作用域是per-container
- Bean(Factory)PostProcessor会被立即实例化，即使定义了`<beans default-lazy-init="true"></beans>`

                    
#### Example:PropertyPlaceholderConfigurer   
```xml
    <bean id="dataSource" class="com.yy.postProcessors.propertyPlaceholder.MyDataSource">
        <property name="url" value="*jdbc.url*"/>
        <property name="username" value="*jdbc.username*"/>
        <property name="password" value="*jdbc.password*"/>
    </bean>
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations" value="classpath:test.properties"/>
        <property name="placeholderPrefix" value="*"/>
        <property name="placeholderSuffix" value="*"/>
    </bean>
```      
```properties
jdbc.url=com.yy.jdbc
jdbc.username=yy
jdbc.password=123456
```
-  PropertyPlaceholderConfigurer在运行时替换对象的属性
-  Spring2.5支持`<context:property-placeholder location="classpath:com/foo/jdbc.properties"/>`方式配置
-  PropertyPlaceholderConfigurer不只搜索指定的properties文件，默认情况下它也会搜索JavaSystem properties文件，如果它找不到你指定的文件。
> systemPropertiesMode
> never (0): Never check system properties
 fallback (1): Check system properties if not resolvable in the specified properties files. This is the default.
 override (2): Check system properties first, before trying the specified properties files. This allows system properties to override any other property source.

      
#### Example:PropertyOverrideConfigurer
                        
类似于PropertyPlaceholderConfigurer，
不同点：原始定义可以具有默认值，或者根本不具有bean属性的值。 
如果重写的Properties文件没有某个bean属性的条目，则使用默认的上下文定义。

并且properties的格式是：
beanName.properties=value

test.properties
```properties
jdbc.url=com.yy.jdbc
jdbc.username=yy
jdbc.password=123456
```
override.properties
```properties
dataSource.driver=com.yy.jdbc.driver
dataSource.url=yy.jdbc://localhost:3306/test
dataSource.password=098765
```
```xml
<bean id="dataSource" class="com.yy.postProcessors.propertyPlaceholder.MyDataSource">
    <property name="url" value="*jdbc.url*"/>
    <property name="username" value="*jdbc.username*"/>
    <property name="password" value="*jdbc.password*"/>
</bean>
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="locations" value="classpath:test.properties"/>
    <property name="placeholderPrefix" value="*"/>
    <property name="placeholderSuffix" value="*"/>
</bean>
<bean class="org.springframework.beans.factory.config.PropertyOverrideConfigurer">
    <property name="locations" value="classpath:override.properties"/>
</bean>
```
结果：{"driver":"com.yy.jdbc.driver","url":"yy.jdbc://localhost:3306/test","username":"yy","password":"098765"}

[<-](#top) 
### 7.8.3 通过FactoryBean自定义实例化逻辑Customizing instantiation logic with a FactoryBean<span id="7.8.3"></span>
如果通过xml配置来创建Bean的逻辑很复杂，可以通过实现org.springframework.beans.factory.FactoryBean接口来处理Bean的复杂实例化逻辑。
```java
public interface FactoryBean<T> {
    // 返回这个工厂创建的实例对象
    T getObject() throws Exception;

    // 返回工厂创建的实例对象的class对象
    Class<?> getObjectType();

    // true：单例，false：原型
    boolean isSingleton();
}
```
通过context.getBean("myBean")获取工厂创建的实例对象;
通过context.getBean("&myBean")获取工厂本身对象;

