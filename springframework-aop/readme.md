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
@Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation() && args(account,..)")
public void validateAccount(Account account) {
    // ...
}
```
- `args(account)`有两个目的：
    1. 限制这个advice方法仅在join point有唯一入参为account时执行
    2. 这使得advice方法获取了join point的入参
- 第二种获取join point入参的方式
```java
@Pointcut("com.xyz.myapp.SystemArchitecture.dataAccessOperation() && args(account,..)")
private void accountDataAccessOperation(Account account) {}

@Before("accountDataAccessOperation(account)")
public void validateAccount(Account account) {
    // ...
}
```
- 获取join point上的注解
