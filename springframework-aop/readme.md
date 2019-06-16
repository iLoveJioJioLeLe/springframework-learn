# 11. Aspect Oriented Programming with Spring

## 11.1 Introduction

- OOP的关键模块是class，AOP的关键模块是aspect

### 11.1.1 AOP概念
- Aspect（切面）：切入到很多类中的一个模块化的关注点, 在Java 应用程序中 事务管理是横切关注点的一个很好例子。
- Join Point（连接点）：程序执行期间的点，比如方法的执行或者异常的处理。在Spring AOP 中连接点表示的是方法的执行。
- Advice（通知）：切面在特定连接点执行的动作。Spring有很多通知类型：before、after、around等。很多AOP 框架包括Spring 把通知作为拦截器并且维护着一个环绕连接点的拦截链。
- Pointcut（切点）：切点定义通知(Advice)可以切入到哪些连接点(Join Point)中,切点的内容就是通知和连接点的匹配规则，通知和一个或多个切点相关联，通知运行在切点匹配的连接点上。
- Introduction（引入）：为某种类型的类动态的添加额外的方法和字段，Spring 可以让你为被观察的对象引入一个新的接口，比如使用‘’引入‘’ 让某个bean 实现IsModified 接口，用于简化缓存。
- Target Object：被一个或多个切面(Aspect)通知(Advice)的对象。因为SpringAOP是运行时代理，所以这个对象是一个被代理对象。
- AOP proxy：一个被AOP框架创建，用来实现切面协议的代理对象。在Spring中，AOP proxy是JDK动态代理或CGLIB代理。
- Weaving（织入）：把切面和目标对象连接在一起，创建出一个新的对象叫做被观察对象（也就是新的目标对象）。这个过程可以在代码编译，加载，或者运行中完成。Spring AOP 像其他纯JAVA AOP 框架一样，在运行时进行织入。

#### 通知类型 Types Of Advice
- Before Advice（前置通知）：运行在连接点之前的通知，没有阻止目标对象方法调用的能力。
- After returning Advice（后置返回通知）：连接的方法正常执行完成，没有发生异常之后执行。
- After throwing Advice（后置异常通知）：方法执行期间抛出异常，非正常结束那么会执行这个通知。
- After (finally) Advice（后置最终通知）：连接点退出的时候执行（无论是否有异常）。
- Around Advice（环绕通知）：环绕连接点的通知。它还负责选择是继续连接点还是通过返回自己的返回值或抛出异常来快速通知方法执行。

Aspect 由多个advice 和 pointCut 组成
一个Aspect 有多个advice
一个advice可以有多个pointCut
一个pointCut可以切入多个targetObject 中的多个joinPoint
（这里指的多个是多个方法,其实Spring AOP 只有一种类型的JoinPoint 就是方法执行连接点 ，AspectJ 有很多种）
一个targetObject 有多个 joinPoint

### 11.1.2 SpringAOP capabilities and goals

- Spring AOP是纯java实现，不需要控制class loader技术，因此适用于Servlet容器或应用服务器
- 目前SpringAOP仅支持method连接点，field拦截并没有实现，尽管支持field拦截不会打破主要的SpringAOP APIs。如果需要field拦截，使用AspectJ。
- SpringAOP不同于其他AOP框架，目标不是完全实现AOP，而是对于AOP实现和SpringIOC提供一个紧密的集成，来帮助解决企业应用的常见问题。

### 11.1.3 AOP Proxies
- SpringAOP默认使用JDK动态代理。对接口实现代理。
- SpringAOP对于非接口实现类使用CGLIB代理。

## 11.2 @AspectJ支持
- Spring使用了与AspectJ5相同的注解，依赖AspectJ的切点解析和匹配。
- SpringAOP运行时还是使用纯SpringAOP，不依赖AspectJ compiler 或 weaver。

### 11.2.1 开启@AspectJ支持
- @AspectJ支持可以用XML或Java配置，依赖aspectjweaver.jar
- Java Config
```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

}
```
- XML Configuration
```xml
<aop:aspectj-autoproxy/>
```

### 11.2.2 声明切面
- 使用Spring component scan，需要额外在切面上声明@Component，@Aspect注解并没有被@Component注解
- SpringAOP不支持切面的切面，一个类被声明为@Aspect时，会从auto-proxying中排除
```xml
<bean id="myAspect" class="org.xyz.NotVeryUsefulAspect">
    <!-- configure properties of aspect here as normal -->
</bean>
```
```java
package org.xyz;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class NotVeryUsefulAspect {

}
```

### 11.2.3 声明切点(pointcut)
- @Pointcut注解的value是常规的AspectJ5切点表达式
```java
@Pointcut("execution(* transfer(..))")// the pointcut expression
// 必须返回void
private void anyOldTransfer() {}// the pointcut signature
```
#### 支持的切点指示符(Pointcut Designators PCD)
- execution：匹配方法执行连接点。我们可以指定包、类或者方法名，以及方法的可见性、返回值和参数类型。这是应用的最为广泛的切入点表达式。例如，execution(* com.apress..TestBean.*(..)表示执行com.apress包中TestBean中的所有方法，无论是何种参数或返回类型
- within：匹配那些在已声明的类型中执行的连接点。例如，within(com.apress..TestBean)匹配从TestBean的方法中产生的调用
- this：通过用bean引用（即AOP代理）的类型跟指定的类型作比较来匹配连接点。例如，this(SimpleBean)将只会匹配在SimpleBean类型的bean上的调用
- target：通过用被调用的bean的类型和指定的类型作比较来匹配连接点。比如target(SimpleBean)将只会匹配在SimpleBean类型bean上方法的调用
- args：通过比较方法的参数类型跟指定的参数类型来匹配连接点。例如，args(String, String)将只会匹配那些有两个String类型参数的方法
- @target：通过检查调用的目标对象（被代理的）是否具有特定注解来匹配连接点。例如，@target(Magic)将会匹配带有@Magic注解的类中的方法调用
- @args：	
跟args相似，不过@args检查的是方法参数的注解而不是它们的类型。例如，@args(NotNull)会匹配所有那些包含一个被标注了@NotNull注解的参数的方法
- @within：跟within相似，这个表达式匹配那些带有特定注解的类中执行的连接点。例如，表达式@within(Magic)将会匹配对带有@Magic注解的类型的bean上方法的调用
- @annotation：通过检查将被调用的方法上的注解是否为指定的注解来匹配连接点。例如，@annotation(Magic)将会匹配所有标有@Magic注解的方法调用
- bean：匹配特定命名的Spring bean，支持通配。`bean(idOrNameOfBean)`
- PCD支持逻辑运算符如：&&（与）、||（或）、!（非）


#### 注意
- 由于SpringAOP框架基于代理的特性，对于JDK proxy只有public接口方法会被拦截；对于CGLIB，只有public和protected方法会被拦截。

#### 例子
- 任意public method
`execution(public * *(..))`
- 任意以'set'开头的方法
`execution(* set*(..))`
- `AccountService`接口中的任意方法
`execution(* com.xyz.service.AccountService.*(..))`
- service包下的类的任意方法
`execution(* com.xyz.service.*.*(..))`
- service包及子包下的类的任意方法
`execution(* com.xyz.service..*.*(..))`
- 任意service包里的join point
`within(com.xyz.service.*)`
- 任意service包或子包里的join point
`within(com.xyz.service..*)`
- 任意实现`AccountService`接口的代理中的join point
`this(com.xyz.service.AccountService)`
- 任意实现`AccountService`接口的目标对象中的join point
`target(com.xyz.service.AccountService)`
- 任意唯一入参是`Serializable`的方法的join point
`args(java.io.Serializable)`
- 任意目标对象有`@Transactional`注解的join point
`@target(org.springframework.transaction.annotation.Transactional)`
- 任意方法有`@Transactional`注解的join point
`@annotation(org.springframework.transaction.annotation.Transactional)`
- 任意唯一入参被`@Classified`注解修饰的方法的join point
`@args(com.xyz.security.Classified)`
- Spring bean 命名为`tradeService`的join point
`bean(tradeService)`
- Spring beans 命名匹配通配表达式`*Service`
`bean(*Service)`

### 11.2.4 声明通知 Advice

#### Before Advice
```java
@Aspect
@Component
public class Aspect1 {
    @Pointcut("execution(public * com.yy.springframework.aop.service.*.*(..))")
    public void pointcut1(){}

    @Before("pointcut1()")
//    @Before("execution(public * com.yy.springframework.aop.service.*.*(..))")
    public void before() {
        System.out.println("before");
    }
}
```

#### After returning Advice
```java
    // returning 绑定方法返回参数
    @AfterReturning(value = "pointcut1()", returning = "user")
    public void afterReturning(User user) {
        System.out.println("afterReturning");
    }
```

#### After throwing advice
```java
    @AfterThrowing(pointcut = "pointcut1()", throwing = "ex")
    public void afterThrowing(Exception ex) {
        System.out.println("afterThrowing");
        System.out.println(ex);
    }
```

#### After(finally) advice
```java
    @After("pointcut1()")
    public void after() {
        System.out.println("after");
    }
```

#### Around advice
```java
    @Around("pointcut1()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        System.out.println("around before");
        User user = new User();
        user.setId(1L);
        Object[] args = new Object[]{user};
        Object o = point.proceed(args);// 执行proceed进入切面方法，入参args数组表示方法入参
        System.out.println("around after");
        return o;
    }
```
```java
public class Bootstrap {
    public static void main(String args[]){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User user = new User();
        user.setId(2L);
        userService.login(user);
    }
}
```

#### Advice parameters

#### Access to the current JoinPoint
- 任意的Advice方法可以声明`org.aspectj.lang.JoinPoint`作为方法的第一个入参
- JoinPoint提供了有用的方法：
    - getArgs()：方法入参
    - getThis()：代理对象
    - getTarget()：目标对象
    - getSignature()：方法签名
    - toString()：被通知的方法描述
    
#### Passing parameters to advice
```java
@Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation() && args(account)")
public void validateAccount(Account account) {
    // ...
}
```
- `args(account)`有两个目的：
    1. 限制这个advice方法仅在join point有唯一入参为account时执行
    2. 这使得advice方法获取了join point的入参
- 第二种获取join point入参的方式
```java
@Pointcut("com.xyz.myapp.SystemArchitecture.dataAccessOperation() && args(account)")
private void accountDataAccessOperation(Account account) {}

@Before("accountDataAccessOperation(account)")
public void validateAccount(Account account) {
    // ...
}
```
#### 获取join point上的注解
- 自定义注解
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable {
    AuditCode value();
}
```
- advice方法
```java
@Before("com.xyz.lib.Pointcuts.anyPublicMethod() && @annotation(auditable)")
public void audit(Auditable auditable) {
    AuditCode code = auditable.value();
    // ...
}
```


#### Advice入参和泛型
- SpringAOP可以处理在类和方法上使用泛型
- 可以通过实际类型来限制拦截的方法
- 集合泛型方法会拦截所有集合，无视泛型
```java
public interface Sample<T> {
    void sampleGenericMethod(T param);
    void sampleGenericCollectionMethod(Collection<T> param);
}
```
```java
@Before("execution(* ..Sample+.sampleGenericMethod(*)) && args(param)")
public void beforeSampleMethod(MyType param) {
    // 这个advice方法会拦截所有Sample的实现类的sampleGenericMethod，且入参类型为MyType的方法
}
```
```java
@Before("execution(* ..Sample+.sampleGenericCollectionMethod(*)) && args(param)")
public void beforeSampleMethod(Collection<MyType> param) {
    // 这个adivce方法会拦截所有Sample的实现类的sampleGenericCollectionMethod，且入参类型为Collection的方法
    // 所以Collection<MyType> Collection<OtherType>都会被拦截
    // 需要声明为Collection<?>并手动检查每个元素的类型
}
```

#### 决定入参名称
- advice执行时的参数绑定依赖于pointcut表达式来声明方法签名，因为Java反射不支持参数名称
- 如果第一个入参是JoinPoint, ProceedingJoinPoint, JoinPoint.StaticPart不需要设置argNames
```java
@Before(value="com.xyz.lib.Pointcuts.anyPublicMethod() && target(bean) && @annotation(auditable)",
        argNames="bean,auditable")
public void audit(Object bean, Auditable auditable) {
    AuditCode code = auditable.value();
    // ... use code and bean
}
```

#### Advice顺序
- SpringAOP与AspectJ的执行顺序一致，在进入Join Point时，优先级越高的advice越先执行，在出Join Point时，优先级越高的advice越后执行。
- 如果两个Advice声明在两个Aspect中，通过`org.springframework.core.Ordered`指定执行顺序，`Ordered.getValue()`值越小，优先级越高。
- 如果两个Advice声明在一个Aspect中，无法确定优先级，建议合并为一个Advice

### 11.2.5 Introductions
- introduction能够用切面让目标对象实现接口，并提供一个默认实现类
```java
public interface IdGeneratorService {
    Long getId();
}
```
```java
public class DefaultIdGeneratorService implements IdGeneratorService {
    public Long getId() {
        Random random = new Random();
        return random.nextLong();
    }
}
```
```java
@Aspect
@Component
@Order(1)
public class IdAspect {

    @DeclareParents(value = "com.yy.springframework.aop.service.*+", defaultImpl = DefaultIdGeneratorService.class)
    public IdGeneratorService idGeneratorService;

}
```
```java
// Introductions
        ProductService service = context.getBean(ProductService.class);
        IdGeneratorService idGeneratorService = (IdGeneratorService) service;
        System.out.println(idGeneratorService.getId());
```

### 11.2.6 Aspect instantiation models
- 默认一个Application context里的aspect都是单例的
- 可以设置perthis()和pertarget(),perthis表示如果某个类的代理类符合其指定的切面表达式，那么就会为每个符合条件的目标类都声明一个切面实例；pertarget表示如果某个目标类符合其指定的切面表达式，那么就会为每个符合条件的类声明一个切面实例
```java
@Aspect("perthis(com.xyz.myapp.SystemArchitecture.businessService())")
public class MyAspect {

    private int someState;

    @Before(com.xyz.myapp.SystemArchitecture.businessService())
    public void recordServiceUsage() {
        // ...
    }

}
```


## 11.3 Schema-based AOP 支持

### 11.3.1 声明Aspect
```xml
<aop:config>
    <aop:aspect id="myAspect" ref="aBean">
        ...
    </aop:aspect>
</aop:config>

<bean id="aBean" class="...">
    ...
</bean>
```

### 11.3.2 声明pointcut
```xml
<aop:config>

    <aop:pointcut id="businessService"
        expression="execution(* com.xyz.myapp.service.*.*(..))"/>

</aop:config>
```

### 11.3.3 声明Advice
#### Before
```xml
<aop:aspect id="beforeExample" ref="aBean">

    <aop:before
        pointcut-ref="dataAccessOperation"
        method="doAccessCheck"/>

    ...

</aop:aspect>
```
#### After returning
```xml
<aop:aspect id="afterReturningExample" ref="aBean">

    <aop:after-returning
        pointcut-ref="dataAccessOperation"
        returning="retVal"
        method="doAccessCheck"/>

    ...

</aop:aspect>
```

#### After throwing
```xml
<aop:aspect id="afterThrowingExample" ref="aBean">

    <aop:after-throwing
        pointcut-ref="dataAccessOperation"
        throwing="dataAccessEx"
        method="doRecoveryActions"/>

    ...

</aop:aspect>

```
#### After(finally) advice
```xml
<aop:aspect id="afterFinallyExample" ref="aBean">

    <aop:after
        pointcut-ref="dataAccessOperation"
        method="doReleaseLock"/>

    ...

</aop:aspect>
```

#### Around advice
```xml
<aop:aspect id="aroundExample" ref="aBean">

    <aop:around
        pointcut-ref="businessService"
        method="doBasicProfiling"/>

    ...

</aop:aspect>
```


#### Advice parameters
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- this is the object that will be proxied by Spring's AOP infrastructure -->
    <bean id="fooService" class="x.y.service.DefaultFooService"/>

    <!-- this is the actual advice itself -->
    <bean id="profiler" class="x.y.SimpleProfiler"/>

    <aop:config>
        <aop:aspect ref="profiler">

            <aop:pointcut id="theExecutionOfSomeFooServiceMethod"
                expression="execution(* x.y.service.FooService.getFoo(String,int))
                and args(name, age)"/>

            <aop:around pointcut-ref="theExecutionOfSomeFooServiceMethod"
                method="profile"/>

        </aop:aspect>
    </aop:config>

</beans>
```

### 11.3.4 Introductions
```xml
<aop:aspect id="usageTrackerAspect" ref="usageTracking">

    <aop:declare-parents
        types-matching="com.xzy.myapp.service.*+"
        implement-interface="com.xyz.myapp.service.tracking.UsageTracked"
        default-impl="com.xyz.myapp.service.tracking.DefaultUsageTracked"/>

    <aop:before
        pointcut="com.xyz.myapp.SystemArchitecture.businessService()
            and this(usageTracked)"
            method="recordUsage"/>

</aop:aspect>
```

### 11.3.5 Aspect instantiation models
- schema-defined aspects 只支持单例，不支持perthis和pertarget

### 11.3.6 Advisors
- advisors概念来自于SpringAOP，在AspectJ中没有等同的概念
- `<aop:advisor>`与事务advice一同连用
```xml
<aop:config>

    <aop:pointcut id="businessService"
        expression="execution(* com.xyz.myapp.service.*.*(..))"/>

    <aop:advisor
        pointcut-ref="businessService"
        advice-ref="tx-advice"/>

</aop:config>

<tx:advice id="tx-advice">
    <tx:attributes>
        <tx:method name="*" propagation="REQUIRED"/>
    </tx:attributes>
</tx:advice>
```
## 11.5 混合aspect类型
- Spring支持同时使用@AspectJ style aspects using the autoproxying support, schema-defined <aop:aspect> aspects, <aop:advisor> declared advisors and even proxies and interceptors defined using the Spring 1.2 style in the same configuration


## 11.6 代理技术
- 如果目标对象至少实现了一个接口，则会使用JDK动态代理；反之，使用CGLIB代理
- 如果使用CGLIB代理，final方法不会被拦截
- 强制使用CGLIB代理的配置方式
1. schema-defined
```xml
<aop:config proxy-target-class="true">
    <!-- other beans defined here... -->
</aop:config>

```
2. @AspectJ autoproxy
```xml
<aop:aspectj-autoproxy proxy-target-class="true"/>
```
- 多个<aop：config />部分在运行时折叠为单个统一的自动代理创建器，它应用指定的任何<aop：config />部分（通常来自不同的XML bean定义文件）的最强代理设置。 这也适用于<tx：annotation-driven />和<aop：aspectj-autoproxy />元素。
- 在<tx：annotation-driven />，<aop：aspectj-autoproxy />或<aop：config />元素上使用proxy-target-class =“true”将强制使用CGLIB代理


### 11.6.1 理解AOP代理
- 代理对象this引用和目标对象this引用
```java
public class SimplePojo implements Pojo {

    public void foo() {
        // this next method invocation is a direct call on the 'this' reference
        // 直接操作目标对象 target 不会被advice拦截
        this.bar();
    }

    public void bar() {
        // some logic...
    }
}
```
```java
public class SimplePojo implements Pojo {

    public void foo() {
        // this works, but... gah!
        // 代理对象 方法会被代理，会执行advice方法
        ((Pojo) AopContext.currentProxy()).bar();
    }

    public void bar() {
        // some logic...
    }
}
```

## 11.7 编程式创建@AspectJ代理
```java
// create a factory that can generate a proxy for the given target object
AspectJProxyFactory factory = new AspectJProxyFactory(targetObject);

// add an aspect, the class must be an @AspectJ aspect
// you can call this as many times as you need with different aspects
factory.addAspect(SecurityManager.class);

// you can also add existing aspect instances, the type of the object supplied must be an @AspectJ aspect
factory.addAspect(usageTracker);

// now get the proxy object...
MyInterfaceType proxy = factory.getProxy();
```

## 11.8 Spring应用使用AspectJ
- 至此覆盖到的章节，是纯SpringAOP，本章将使用AspectJ compiler/weaver


### 11.8.1 Spring用Aspectj对DomainObject进行依赖注入
- spring-aspects.jar提供了任意对象的注解驱动依赖注入，即使是ioc容器外的对象，如`new`创建的对象
- 使用Aspecj进行依赖注入
1. @Configurable注释标记一个类符合Spring驱动配置的条件
```java
package com.xyz.myapp.domain;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class Account {
    // ...
}
```
2. 开启spring-configured
```java
@Configuration
@EnableSpringConfigured
public class AppConfig {

}
```
备注：XML-based配置
```xml
<context:spring-configured/>
```
3. 加载时织入
3-1. VM option
`-javaagent:/path/to/aspectjweaver-1.8.9.jar`
3-2. META-INF/aop.xml
```xml
<!DOCTYPE aspectj PUBLIC "-//AspectJ//DTD//EN" "http://www.eclipse.org/aspectj/dtd/aspectj.dtd">

<aspectj>
    <weaver>
        <!-- 需要运行时植入切面的类 -->
        <include within="com.yy.springframework.aop.model.*"/>
    </weaver>
</aspectj>
```

### 11.8.2 Other Spring aspects for AspectJ
 
- @Transactional注解由AnnotationTransactionAspect解释
- 需要注释实现类或实现类中的方法，而不是实现的接口


### 11.8.3 用SpringIOC配置AspectJ aspects
- 如果@AspectJ切面，一部分通过AspectJ织入，一部分通过SpringAOP使用，那么使用SpringAOP的aspects可以使用如下配置
```xml
<aop:aspectj-autoproxy>
    <aop:include name="thisBean"/>
    <aop:include name="thatBean"/>
</aop:aspectj-autoproxy>
```


### 11.8.3 Spring框架中加载时织入AspectJ Load-time weaving AspectJ
- 加载时织入，是指在将AspectJ切面加载到JVM中时，将其织入到应用程序的类文件中的过程

