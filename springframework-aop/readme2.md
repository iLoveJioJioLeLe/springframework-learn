# 12. Spring AOP APIs

## 12.1 介绍

之前的章节介绍了Spring支持的@AspectJ和schema-based切面定义。
本章节将介绍底层SpringAOP APIs。


## 12.2 Pointcut API

### 12.2.1 概念
- Spring的pointcut允许多个advice依赖同一个pointcut
- `org.springframework.aop.Pointcut`
```java
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();

}
```
- `ClassFilter`接口限制pointcut针对的目标class，`matches()`方法返回true则目标class被匹配
```java
public interface ClassFilter {

    boolean matches(Class clazz);
}
```
- `MethodMatcher`接口
```java
public interface MethodMatcher {
    // 用于测试这个pointcut是否匹配对应的class中的method，
    // 本方法在AOP代理创建时执行，而非每个目标方法执行时都测试是否匹配
    boolean matches(Method m, Class targetClass);

    // 如果2个入参的matches方法返回true，且该方法返回true，
    // 则3个参数的matches方法将每个目标方法执行时都检测是否匹配
    boolean isRuntime();

    // 对目标方法的入参进行匹配
    // 大部分MethodMatcher都是static的，也就是isRuntime()方法总返回false
    // 该方法将不会被执行
    boolean matches(Method m, Class targetClass, Object[] args);
}
```

### 12.2.2 Operations on pointcuts
Spring支持操作pointcuts：并集和交集
- 并集：方法匹配任意pointcut
- 交集：方法匹配所有pointcut
- 并集更常用
- pointcuts可以通过静态方法组装:`org.springframework.aop.support.Pointcuts`或`ComposablePointcut`
- 不过使用AspectJ的切点表达式更简单

### 12.2.3 AspectJ expression pointcuts
从Spring2.0开始使用`org.springframework.aop.aspectj.AspectJExpressionPointcut`
来支持解析AspectJ切点表达式字符串

### 12.2.4 便捷的pointcut实现类

#### 静态pointcuts Static pointcuts
- 基于方法和目标class，不支持方法参数
- 性能好
- Spring只在第一次执行的时候计算表达式一次
#### Static pointcuts:Regular expression pointcuts
- JdkRegexpMethodPointcut 是一个通用的正则表达式pointcut
```xml
    <bean id="settersAndAbsquatulatePointcut"
              class="org.springframework.aop.support.JdkRegexpMethodPointcut">
          <property name="patterns">
              <list>
                  <value>.*set.*</value>
                  <value>.*absquatulate</value>
              </list>
          </property>
      </bean>
```
- Spring提供了一个便捷的class`RegexpMethodPointcutAdvisor`，它引用了一个Advice
```xml
<bean id="settersAndAbsquatulateAdvisor"
        class="org.springframework.aop.support.RegexpMethodPointcutAdvisor">
    <property name="advice">
        <ref bean="beanNameOfAopAllianceInterceptor"/>
    </property>
    <property name="patterns">
        <list>
            <value>.*set.*</value>
            <value>.*absquatulate</value>
        </list>
    </property>
</bean>

```
#### Static pointcuts:Attribute-driven pointcuts
> An important type of static pointcut is a metadata-driven pointcut. This uses the values of metadata attributes: typically, source-level metadata.

#### 动态pointcuts Dynamic pointcuts
- 动态pointcuts支持方法参数
- 动态pointcuts每次方法执行时都会重新计算，所以比静态pointcuts耗费更多资源
- 主要的例子是`control flow`pointcut

#### Dynamic pointcuts:Control flow pointcuts
- In Java 1.4, the Control flow pointcuts cost is about 5 times that of other dynamic pointcuts.
- `org.springframework.aop.support.ControlFlowPointcut`

### 12.2.5 Pointcut superclasses
- Spring提供了常用的pointcut superclasses来帮助实现自定义pointcuts
- 由于静态pointcuts更常用，一般继承StaticMethodMatcherPointcut
```java
class TestStaticPointcut extends StaticMethodMatcherPointcut {

    public boolean matches(Method m, Class targetClass) {
        // return true if custom criteria match
    }
}
```
### 12.2.6 自定义pointcut
> Because pointcuts in Spring AOP are Java classes, rather than language features (as in AspectJ) it’s possible to declare custom pointcuts, whether static or dynamic. Custom pointcuts in Spring can be arbitrarily complex. However, using the AspectJ pointcut expression language is recommended if possible.

## 12.3 Advice API

### 12.3.1 Advice lifecycles
- 一个Advice实例可以对于所有advised objects公用，也可以只对应一个advised object，这取决于per-class还是per-instance advice
- Per-class更常用，对于那些不依赖状态的被代理的类
- Per-instance适用于introductions来支持mixins(混入)
- 支持在同一个AOP代理中，同时使用shared和per-instance advice

### 12.3.2 Advice 类型

#### Interception around advice
- Spring中最基本的Advice类型是Interception around advice
- Spring符合AOP Alliance接口，使用方法拦截实现around advice需要实现以下接口
```java
package org.aopalliance.intercept;

public interface MethodInterceptor extends Interceptor {
    // MethodInvocation暴露了
    // 1.被执行的method
    // 2.目标join point
    // 3.AOP代理
    // 4.method的参数
    Object invoke(MethodInvocation var1) throws Throwable;
}
```
- 简单实现示例
```java
public class DebugInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before: invocation=[" + invocation + "]");
        Object rval = invocation.proceed();
        System.out.println("Invocation returned");
        return rval;
    }
}
```


#### Before advice
```java
public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method m, Object[] args, Object target) throws Throwable;
}
```

#### Throws advice
- ThrowsAdvice只是一个标记接口，继承AfterAdvice接口
- ThrowsAdvice表明，实现类有一个或多个throws advice方法，方法签名如下：
`afterThrowing([Method, args, target], subclassOfThrowable)`。只有最后一个参数是必须的，方法签名可能有一个或四个参数，取决于advice方法是否关注method和arguments
- 示例
```java
public class RemoteThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(RemoteException ex) throws Throwable {
        // Do something with remote exception
    }
}
```
```java
public class ServletThrowsAdviceWithArguments implements ThrowsAdvice {

    public void afterThrowing(Method m, Object[] args, Object target, ServletException ex) {
        // Do something with all arguments
    }
}
```
```java
public static class CombinedThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(RemoteException ex) throws Throwable {
        // Do something with remote exception
    }

    public void afterThrowing(Method m, Object[] args, Object target, ServletException ex) {
        // Do something with all arguments
    }
}
```
- 如果throws-advice自己抛出异常，会override原来的异常(i.e. change the exception thrown to the user)
  被重写的异常是RuntimeException。但是如果抛出的是一个受查异常，必须要匹配目标方法声明的异常。


#### After Returning advice
```java
public interface AfterReturningAdvice extends Advice {

    void afterReturning(Object returnValue, Method m, Object[] args, Object target)
            throws Throwable;
}
```

#### Introduction advice(混入)
- Introduction需要`IntroductionAdvisor`和`IntroductionInterceptor`
```java
public interface IntroductionInterceptor extends MethodInterceptor {

    boolean implementsInterface(Class intf);
}
```
- 从AOP Alliance MethodInterceptor接口继承的invoke（）方法必须实现引入：即，如果被调用的方法在引入的接口上，则引入拦截器负责处理方法调用 - 它不能调用proceed（）。
```java
public interface IntroductionAdvisor extends Advisor, IntroductionInfo {

    ClassFilter getClassFilter();
    // 校验被引入的接口是否被配置的IntroductionInterceptor实现
    void validateInterfaces() throws IllegalArgumentException;
}

public interface IntroductionInfo {
    // 需要引入的接口
    Class[] getInterfaces();
}
```
示例：
```java
// 被引入的接口
public interface Lockable {
    void lock();
    void unlock();
    boolean locked();
}
```
```java
public class LockMixin extends DelegatingIntroductionInterceptor implements Lockable {

    private boolean locked;

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    public boolean locked() {
        return this.locked;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (locked() && invocation.getMethod().getName().indexOf("set") == 0) {
            throw new LockedException();
        }
        return super.invoke(invocation);
    }

}
```
```java
public class LockMixinAdvisor extends DefaultIntroductionAdvisor {

    public LockMixinAdvisor() {
        super(new LockMixin(), Lockable.class);
    }
}
```

## 12.4 Advisor API
- Advisor是一个切面包含了单个advice实例和一个pointcut表达式
- `org.springframework.aop.support.DefaultPointcutAdvisor`是常用的advisor class，
  例如，它可以同`MethodInterceptor`，`BeforeAdvice`或`ThrowsAdvice`使用
- 可以混合使用advisor和不同类型的advice在同一个AOP代理中，
  例如，你可以使用interception around advice，throws advice，before advice在同一个proxy配置，
  Spring会自动创建一个interceptor chain


## 12.5 使用ProxyFactoryBean创建AOP代理

### 12.5.1 Basics
- 与其他Spring FactoryBean实现一样，如果定义名为foo的ProxyFactoryBean，
  那么引用foo的对象不是ProxyFactoryBean实例本身，而是ProxyFactoryBean的getObject()方法创建目标对象。
  此方法将创建包装目标对象的AOP代理。
- adivces和pointcuts被ioc容器管理的优点是，他可以引用容器中其他bean，通过DI

### 12.5.2 JavaBean properties
- `ProxyFactoryBean`properies
   - 指定代理目标
   - 指定是否使用CGLIB
- 一些关键的properties继承自`org.springframework.aop.framework.ProxyConfig`
   - proxyTargetClass：如果true，使用CGLIB代理，反之使用JDK代理
   - optimize：控制是否积极优化应用于通过CGLIB创建的代理，对JDK动态代理无影响
   - frozen：如果true，则不允许改变配置，用于控制调用者在代理被创建后禁止操作代理。默认是false。
   - exposeProxy：决定是否将当前代理暴露在`ThreadLocal`中，如果true，目标对象可以使用`AopContext.currentProxy()`获得当前代理。
- 其他特殊的`ProxyFactoryBean`properties
   - proxyInterfaces：代理的接口名称数组，如果不设置，将使用CGLIB代理
   - interceptorNames：Advisor，interceptor或其它advice的名字数组。由数据先后顺序，决定拦截顺序。
   - singleton：getObject()返回的实例是否单例


### 12.5.3 JDK- and CGLIB-based proxies
- 如果目标对象的class没有实现接口，使用CGLIB代理，即使`proxyTargetClass`设置为false
- 如果目标类实现一个或多个接口，则创建的代理类型取决于ProxyFactoryBean的配置
- `proxyTargetClass`设置为true，即使`proxyInterfaces`设置了值，也会使用CGLIB代理
- 如果目标class实现了超过配置中`proxyInterfaces`的其他接口，这些其他接口不会被代理实现
- 如果不配置`proxyInterfaces`，但是目标class实现了一个或多个接口，`ProxyFactoryBean`会自动检测到目标class实现的接口，并使用JDK动态代理，代理会实现所有目标class的接口。


### 12.5.4 Proxying interfaces
```xml
<bean id="personTarget" class="com.mycompany.PersonImpl">
    <property name="name" value="Tony"/>
    <property name="age" value="51"/>
</bean>

<bean id="myAdvisor" class="com.mycompany.MyAdvisor">
    <property name="someProperty" value="Custom string property value"/>
</bean>

<bean id="debugInterceptor" class="org.springframework.aop.interceptor.DebugInterceptor">
</bean>

<bean id="person"
    class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="proxyInterfaces" value="com.mycompany.Person"/>

    <property name="target" ref="personTarget"/>
    <property name="interceptorNames">
        <list>
            <value>myAdvisor</value>
            <value>debugInterceptor</value>
        </list>
    </property>
</bean>
```


### 12.5.5 Proxying classes
- CGLIB运行时生成目标class子类，来织入advice
- Final方法不能被advised
- 无需引入CGLIB依赖，Spring3.2开始CGLIB被加入到spring-core中

### 12.5.6 使用'global'advisors
- interceptorNames添加星号
```xml
<bean id="proxy" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target" ref="service"/>
    <property name="interceptorNames">
        <list>
            <value>global*</value>
        </list>
    </property>
</bean>

<bean id="global_debug" class="org.springframework.aop.interceptor.DebugInterceptor"/>
<bean id="global_performance" class="org.springframework.aop.interceptor.PerformanceMonitorInterceptor"/>
```

## 12.6 简洁proxy定义
- 尤其是定义事务代理时，会使用多种相似的代理定义。使用parent and child bean definition和inner bean definitions会使代理定义更干净和简洁。
- parent，template
```xml
<bean id="txProxyTemplate" abstract="true"
        class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
    <property name="transactionManager" ref="transactionManager"/>
    <property name="transactionAttributes">
        <props>
            <prop key="*">PROPAGATION_REQUIRED</prop>
        </props>
    </property>
</bean>
```
- child
```xml
<bean id="myService" parent="txProxyTemplate">
    <property name="target">
        <bean class="org.springframework.samples.MyServiceImpl">
        </bean>
    </property>
</bean>
```
```xml
<bean id="mySpecialService" parent="txProxyTemplate">
    <property name="target">
        <bean class="org.springframework.samples.MySpecialServiceImpl">
        </bean>
    </property>
    <property name="transactionAttributes">
        <props>
            <prop key="get*">PROPAGATION_REQUIRED,readOnly</prop>
            <prop key="find*">PROPAGATION_REQUIRED,readOnly</prop>
            <prop key="load*">PROPAGATION_REQUIRED,readOnly</prop>
            <prop key="store*">PROPAGATION_REQUIRED</prop>
        </props>
    </property>
</bean>
```

## 12.7 用ProxyFactory编程式创建AOP代理
```java
ProxyFactory factory = new ProxyFactory(myBusinessInterfaceImpl);// 参数是目标对象
factory.addAdvice(myMethodInterceptor);// interceptors
factory.addAdvisor(myAdvisor);// advisors
MyBusinessInterface tb = (MyBusinessInterface) factory.getProxy();// 获得代理对象
```

## 12.8 操作advised objects
- 任何AOP代理都可以被转换为`org.springframework.aop.framework.Advised`接口，用于操作advised objects
```java
public interface Advised extends TargetClassAware {
    // ...
    Advisor[] getAdvisors();

    void addAdvisor(Advisor var1) throws AopConfigException;

    void addAdvisor(int var1, Advisor var2) throws AopConfigException;

    boolean removeAdvisor(Advisor var1);

    void removeAdvisor(int var1) throws AopConfigException;

    int indexOf(Advisor var1);

    boolean replaceAdvisor(Advisor var1, Advisor var2) throws AopConfigException;

    void addAdvice(Advice var1) throws AopConfigException;

    void addAdvice(int var1, Advice var2) throws AopConfigException;

    boolean removeAdvice(Advice var1);

    int indexOf(Advice var1);

    String toProxyConfigString();
}

```

```java
Advised advised = (Advised) myObject;
Advisor[] advisors = advised.getAdvisors();
int oldAdvisorCount = advisors.length;
System.out.println(oldAdvisorCount + " advisors");

// Add an advice like an interceptor without a pointcut
// Will match all proxied methods
// Can use for interceptors, before, after returning or throws advice
advised.addAdvice(new DebugInterceptor());

// Add selective advice using a pointcut
advised.addAdvisor(new DefaultPointcutAdvisor(mySpecialPointcut, myAdvice));

assertEquals("Added two advisors", oldAdvisorCount + 2, advised.getAdvisors().length);
```
- 代理的frozen属性，可以从`Advised`接口的`isFrozen()`方法得到，如果返回true，那么所有advice的改动操作会导致`AopConfigException`

## 12.9 使用'auto-proxy'功能

### 12.9.1 Autoproxy bean 定义

#### BeanNameAutoProxyCreator
- 主要作用：相同配置作用于多个目标对象（通过beanName通配）
```xml
<bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
    <property name="beanNames" value="jdk*,onlyJdk"/>
    <property name="interceptorNames">
        <list>
            <value>myInterceptor</value>
        </list>
    </property>
</bean>
```

#### DefaultAdvisorAutoProxyCreator
- DefaultAdvisorAutoProxyCreator会自动计算advisor中的pointcut来决定advisor中的advice是否应用于business object
- 主要作用：相同advice作用于多个business objects
- 例子：如果businessObject1和businessObject2匹配transactionInterceptor或customAdvisor的pointcut，则将返回代理对象
```xml
<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>

<bean class="org.springframework.transaction.interceptor.TransactionAttributeSourceAdvisor">
    <property name="transactionInterceptor" ref="transactionInterceptor"/>
</bean>

<bean id="customAdvisor" class="com.mycompany.MyAdvisor"/>

<bean id="businessObject1" class="com.mycompany.BusinessObject1">
    <!-- Properties omitted -->
</bean>

<bean id="businessObject2" class="com.mycompany.BusinessObject2"/>
```

## 12.10 TargetSources

- 其实ProxyFactoryBean配置时，target这个属性最终会通过AdvisedSupport的setTarget(Object obj)方法转换为SingletonTargetSource实例作为this.targetSource
```java
    public void setTarget(Object target) {
		setTargetSource(new SingletonTargetSource(target));
	}
```
### 12.10.1 Hot swappable target sources
- `org.springframework.aop.target.HotSwappableTargetSource`允许AOP代理目标切换，但使用者任然保持引用
- `HotSwappableTargetSource`是线程安全的
```java
    /**
	 * Swap the target, returning the old target object.
	 * @param newTarget the new target object
	 * @return the old target object
	 * @throws IllegalArgumentException if the new target is invalid
	 */
	public synchronized Object swap(Object newTarget) throws IllegalArgumentException {
		Assert.notNull(newTarget, "Target object must not be null");
		Object old = this.target;
		this.target = newTarget;
		return old;
	}
```
- 使用方式
```java
HotSwappableTargetSource swapper = (HotSwappableTargetSource) beanFactory.getBean("swapper");
Object oldTarget = swapper.swap(newTarget);
```
```xml
<bean id="initialTarget" class="mycompany.OldTarget"/>

<bean id="swapper" class="org.springframework.aop.target.HotSwappableTargetSource">
    <constructor-arg ref="initialTarget"/>
</bean>

<bean id="swappable" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="targetSource" ref="swapper"/>
</bean>
```
- swap()方法改变了代理所代理的目标对象，引用这个代理(proxy)的客户端并不会感知到变化，可以立即获得新的目标对象

### 12.10.2 Pooling target sources
- Spring pooling可以支持任意POJO
- 需要commons-pool依赖
```xml
<bean id="businessObjectTarget" class="com.mycompany.MyBusinessObject"
        scope="prototype">
    <!-- 必须是原型对象，这允许PoolingTargetSource动态新增目标对象实例 -->
    ... properties omitted
</bean>

<bean id="poolTargetSource" class="org.springframework.aop.target.CommonsPool2TargetSource">
    <property name="targetBeanName" value="businessObjectTarget"/>
    <property name="maxSize" value="25"/>
</bean>

<bean id="businessObject" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="targetSource" ref="poolTargetSource"/>
    <property name="interceptorNames" value="myInterceptor"/>
</bean>
```
- 可以通过配置使得任意pooled object可以转换为`org.springframework.aop.target.PoolingConfig`，比如获取pool size
```xml
<bean id="poolConfigAdvisor" class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
    <property name="targetObject" ref="poolTargetSource"/>
    <property name="targetMethod" value="getPoolingConfigMixin"/>
</bean>
```
```java
PoolingConfig conf = (PoolingConfig) beanFactory.getBean("businessObject");
System.out.println("Max pool size is " + conf.getMaxSize());
```

### 12.10.3 Prototype target sources
- 设置一个原型target source类似于pooling TargetSource，在这种情况下，每次方法执行都会生成一个新的目标对象
```xml
<bean id="prototypeTargetSource" class="org.springframework.aop.target.PrototypeTargetSource">
    <property name="targetBeanName" ref="businessObjectTarget"/>
</bean>
```

### 12.10.4 ThreadLocal target sources
- `ThreadLocal` target sources，当你需要为每个request创建一个实例时会很实用
```xml
<bean id="threadlocalTargetSource" class="org.springframework.aop.target.ThreadLocalTargetSource">
    <property name="targetBeanName" value="businessObjectTarget"/>
</bean>
```

## 12.11 定义新的Advice类型
- `org.springframework.aop.framework.adapter`包是一个SPI包，支持新的自定义advice类型，不用改变核心框架
- 唯一约束是必须实现`org.aopalliance.aop.Advice`标记接口