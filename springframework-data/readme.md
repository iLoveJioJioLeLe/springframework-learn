# 17. 事务管理

## 17.1 介绍Spring事务管理

## 17.2 Spring事务优势

### 17.2.1 全局事务
- 全局事务使您可以使用多个事务资源，通常是关系数据库和消息队列
- 通过JTA实现，且只在应用服务器环境中可行

### 17.2.2 本地事务
- 本地事务容易使用，但是不能跨事务来源

### 17.2.3 Spring consistent programming model
- Spring解决了全局和本地事务的劣势，它允许开发者在任何环境中，使用一个consistent programming model
- Spring提供了两种事务管理：1.声明式2.编程式，绝大多数用户使用声明式，并且也是被建议使用的

## 17.3 理解Spring事务抽象
- 主要三个接口：PlatformTransactionManager、TransactionDefinition、TransactionStatus
- Spring事务抽象的关键是事务策略：`org.springframework.transaction.PlatformTransactionManager`
```java
public interface PlatformTransactionManager {

    TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException;

    void commit(TransactionStatus status) throws TransactionException;

    void rollback(TransactionStatus status) throws TransactionException;
}
```
- TransactionDefinition接口
```java
public interface TransactionDefinition {
    // 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中
    int PROPAGATION_REQUIRED = 0;
    // 支持当前事务，如果当前没有事务，就以非事务方式执行
    int PROPAGATION_SUPPORTS = 1;
    // 使用当前的事务，如果当前没有事务，就抛出异常
    int PROPAGATION_MANDATORY = 2;
    // 新建事务，如果当前存在事务，把当前事务挂起
    int PROPAGATION_REQUIRES_NEW = 3;
    // 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起
    int PROPAGATION_NOT_SUPPORTED = 4;
    // 以非事务方式执行，如果当前存在事务，则抛出异常
    int PROPAGATION_NEVER = 5;
    // 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作
    // 与PROPAGATION_REQUIRES_NEW的区别：
    // 内层事务依赖外层事务，外层事务成功，内层事务才会提交，否则全部回滚;而REQUIRES_NEW是完全没关系的两个事务
    int PROPAGATION_NESTED = 6;
    int ISOLATION_DEFAULT = -1;
    int ISOLATION_READ_UNCOMMITTED = 1;
    int ISOLATION_READ_COMMITTED = 2;
    int ISOLATION_REPEATABLE_READ = 4;
    int ISOLATION_SERIALIZABLE = 8;
    int TIMEOUT_DEFAULT = -1;

    // 事务传播特性
    int getPropagationBehavior();

    // 事务隔离级别
    int getIsolationLevel();

    // 事务超时时间
    int getTimeout();

    // 是否只读
    boolean isReadOnly();

    // 事务名称，在Spring中是全类名+'.'+方法名
    String getName();
}
```
- TransactionStatus提供了简单的方式控制事务执行和查询事务状态，在JavaEE事务上下文中，与当前线程联系在一起
```java
public interface TransactionStatus extends SavepointManager {

    boolean isNewTransaction();

    boolean hasSavepoint();

    void setRollbackOnly();

    boolean isRollbackOnly();

    void flush();

    boolean isCompleted();

}
```
- `PlatformTransactionManager`实现类需要工作环境信息如：JDBC、JTA、Hibernate等，下面是本地`PlatformTransactionManager`配置
1. 基于JDBC
```xml
<!-- JDBC数据源配置 -->
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${jdbc.driverClassName}" />
    <property name="url" value="${jdbc.url}" />
    <property name="username" value="${jdbc.username}" />
    <property name="password" value="${jdbc.password}" />
</bean>
<!-- PlatformTransactionManager配置 -->
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
```
2. 基于JTA，如果在JavaEE容器中使用JTA，`DataSource`由JNDI提供，配置如下
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:jee="http://www.springframework.org/schema/jee"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/jee
        https://www.springframework.org/schema/jee/spring-jee.xsd">

    <jee:jndi-lookup id="dataSource" jndi-name="jdbc/jpetstore"/>

    <bean id="txManager" class="org.springframework.transaction.jta.JtaTransactionManager" />
</beans>
```
3. 基于Hibernate
```xml
<!-- DataSource配置同JDBC -->
<!-- Hibernate SessionFactory配置-->
<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mappingResources">
        <list>
            <value>org/springframework/samples/petclinic/hibernate/petclinic.hbm.xml</value>
        </list>
    </property>
    <property name="hibernateProperties">
        <value>
            hibernate.dialect=${hibernate.dialect}
        </value>
    </property>
</bean>
<!-- PlatformTransactionManager配置 -->
<bean id="txManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
    <property name="sessionFactory" ref="sessionFactory"/>
</bean>
```
## 17.4 事务资源同步
- 事务管理器如何与相关资源同步，如DataSourceTransactionManager到JDBC DataSource，HibernateTransactionManager到Hibernate SessionFactory
- 直接或间接使用持久性API（如JDBC、Hibernate）的代码如何确保正确创建，重用和清理这些资源
- 如何通过相关的PlatformTransactionManager触发事务同步

### 17.4.1 High-level同步方法
- 使用ORM API或模板方法JdbcTemplate
> The preferred approach is to use Spring’s highest level template based persistence integration APIs or to use native ORM APIs with transaction- aware factory beans or proxies for managing the native resource factories. These transaction-aware solutions internally handle resource creation and reuse, cleanup, optional transaction synchronization of the resources, and exception mapping. Thus user data access code does not have to address these tasks, but can be focused purely on non-boilerplate persistence logic. Generally, you use the native ORM API or take a template approach for JDBC access by using the JdbcTemplate. These solutions are detailed in subsequent chapters of this reference documentation.

### 17.4.2 Low-level同步方法
- `DataSourceUtils`(for JDBC),
   `EntityManagerFactoryUtils` (for JPA), 
   `SessionFactoryUtils` (for Hibernate), 
   `PersistenceManagerFactoryUtils` (for JDO)
- 当用代码直接处理本机持久性API的资源类型时，可以使用这些类来获得正确的Spring托管实例，同步事务
  如：`Connection conn = DataSourceUtils.getConnection(dataSource);`
  如果事务已经存在，那么将返回对应的connection；否则会创建新的connection(可选，并且同步事务)

### 17.4.3 TransactionAwareDataSourceProxy
- 最底层
- 目标DataSource的代理，让目标DataSource感知到Spring事务管理

## 17.5 声明式事务管理
- 使用SpringAOP实现Spring声明式事务管理

### 17.5.1 理解Spring声明式事务实现
- 调用事务管理的方法
  WayIn:Caller -> AOP Proxy -> Transaction Advisor -> Custom Advisor(s) -> TargetMethod
  WayOut:Caller <- AOP Proxy <- Transaction Advisor <- Custom Advisor(s) <- TargetMethod

### 17.5.2 Example
1. DataSource
2. PlatformTransactionManager
3. tx:advice
3.1. transaction-manager = PlatformTransactionManager
3.2. tx:attribute 对于不同的方法使用不同的策略(propagation、isolation、read-only)
4. aop:config
4-1. aop:pointcut
4-2. aop:advisor advice-ref pointcut-ref
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- this is the service object that we want to make transactional -->
    <bean id="fooService" class="x.y.service.DefaultFooService"/>

    <!-- the transactional advice (what 'happens'; see the <aop:advisor/> bean below) -->
    <tx:advice id="txAdvice" transaction-manager="txManager">
        <!-- the transactional semantics... -->
        <tx:attributes>
            <!-- all methods starting with 'get' are read-only -->
            <tx:method name="get*" read-only="true"/>
            <!-- other methods use the default transaction settings (see below) -->
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>

    <!-- ensure that the above transactional advice runs for any execution
        of an operation defined by the FooService interface -->
    <aop:config>
        <aop:pointcut id="fooServiceOperation" expression="execution(* x.y.service.FooService.*(..))"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="fooServiceOperation"/>
    </aop:config>

    <!-- don't forget the DataSource -->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <property name="driverClassName" value="oracle.jdbc.driver.OracleDriver"/>
        <property name="url" value="jdbc:oracle:thin:@rj-t42:1521:elvis"/>
        <property name="username" value="scott"/>
        <property name="password" value="tiger"/>
    </bean>

    <!-- similarly, don't forget the PlatformTransactionManager -->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- other <bean/> definitions here -->

</beans>
```

### 17.5.3 回滚声明式事务
- 回滚事务的推荐方法：在当前事务的上下文中抛出`Exception`
- 默认配置下，运行时非受查异常会导致事务回滚
- 通过配置
  `<tx:method rollback-for="AnException">`设置指定异常回滚，
  `<tx:method no-rollback-for="AnException"/>`设置指定异常不回滚
- 编程式回滚
```java
public void resolvePosition() {
    try {
        // some business logic...
    } catch (NoProductInStockException ex) {
        // trigger rollback programmatically
        // TransactionAspectSupport.currentTransactionStatus()从线程变量中获取TranscationStatus实例
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
```
### 17.5.4 为不同的bean配置不同的事务语义
- 对于不同的pointcut配置不同的advice-ref（不同的tx-advice）

### 17.5.5 tx:advice配置
- tx:advice默认配置
1. 事务传播Propagation：REQUIRED
2. 事务隔离级别：DEFAULT
3. 只读：false
4. 超时：无
5. 回滚机制：任何运行时异常导致回滚，任何受查异常不会导致回滚

#### 通过tx:method来改变默认配置
| Attribute | Required? | Default | Desc |
| - | - | - | - |
| `name` | Y |  | 方法名，支持通配 |
| `propagation` | N | REQUIRED | 事务传播特性 |
| `isolation` | N | DEFAULT | 事务隔离级别，仅当传播特性为REQUIRED或REQUIREDS_NEW时有用 |
| `timeout` | N | -1 | 事务超时时间(秒)，仅当传播特性为REQUIRED或REQUIREDS_NEW时有用 |
| `read-only` | N | false | 可读可写(false)或只读(true)，仅当传播特性为REQUIRED或REQUIREDS_NEW时有用 |
| `rollback-for` | N |  | 异常引起回滚，用逗号分割 |
| `no-rollback-for` | N | | 异常不引起回滚，用逗号分割 |

### 17.5.6 使用@Transactional
```xml
<!-- from the file 'context.xml' -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- this is the service object that we want to make transactional -->
    <bean id="fooService" class="x.y.service.DefaultFooService"/>
    <!-- enable the configuration of transactional behavior based on annotations -->
    <tx:annotation-driven transaction-manager="txManager"/><!-- a PlatformTransactionManager is still required -->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!-- (this dependency is defined somewhere else) -->
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!-- other <bean/> definitions here -->
</beans>
```
- tx:annotation-driven或@EnableTransactionManagement开启@Transactional支持，作用域是当前application context
- 如果PlatformTransactionManager这个Bean的name不是transactionManager，那么`tx:annotation-driven transaction-manager`就需要配置，否则可以省略
- @Transactional可以配置在
 1. 接口
 2. 接口方法
 3. 实体类
 4. 实体类public方法（tx:annotation-driven默认配置是JDK动态代理）
- 建议配置在实体类和实体类public方法上，虽然默认tx:annotation-driven使用JDK动态代理，但是如果proxy-target-class="true"将使用CGLIB代理，这样将使得事务失效（可以使用mode="aspectj"）
- self-invocation（使用this关键字调用方法）会导致事务失效，因为事务拦截作用于代理对象，而不是目标对象(this)
#### 注解驱动事务配置
| XML Attribute | Annotation Attribute | Default | Desc |
| - | - | - | - |
| `transaction-manager` | 无(见`TransactionManagementConfigurer`) | transactionManager | TransactionManager的BeanName |
| `mode` | `mode` | proxy | 默认代理模式使用SpringAOP，另外的可选模式是'aspectj'，使用AspectJ织入，依赖spring-aspects.jar |
| `proxy-target-class` | proxyTargetClass | false | 默认使用JDK动态代理，配置为true则使用CGLIB |
| `order` | `order` | Ordered.LOWEST_PRECEDENCE | 事务advice的切面顺序 |

### 17.5.7 事务传播
见TransactionDefinition

### 17.5.8 Advising transactional operations
- 自定义advice和transactional advice配置，通过order来设置先后


### 17.5.9 Using @Transactional with AspectJ
- 使用AspectJ配置@Transactional
    1. 配置@Transactional在类或方法上
    2. 通过`org.springframework.transaction.aspectj.AnnotationTransactionAspect`织入
    3. 配置transaction manager
方式一：
        `<tx:annotation-driven mode="aspectj"/>`
方式二：
```java
// construct an appropriate transaction manager
DataSourceTransactionManager txManager = new DataSourceTransactionManager(getDataSource());

// configure the AnnotationTransactionAspect to use it; this must be done before executing any transactional methods
AnnotationTransactionAspect.aspectOf().setTransactionManager(txManager);
```
   4. 通过AspectJ构建工程或使用load-time weaving

## 17.6 编程式事务管理
- 两种编程式事务管理
1. 使用TransactionTemplate（推荐）
2. 直接使用PlatformTransactionManager实现类

### 17.6.1 使用TransactionTemplate
- 采用类似于JdbcTemplate的方法
- transactionTemplate.execute执行有返回值的事务
```java
public class SimpleService implements Service {

    // single TransactionTemplate shared amongst all methods in this instance
    private final TransactionTemplate transactionTemplate;

    // use constructor-injection to supply the PlatformTransactionManager
    public SimpleService(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public Object someServiceMethod() {
        return transactionTemplate.execute(new TransactionCallback() {
            // the code in this method executes in a transactional context
            public Object doInTransaction(TransactionStatus status) {
                updateOperation1();
                return resultOfUpdateOperation2();
            }
        });
    }
}
```
- transactionTemplate.execute执行无返回值事务
```java
transactionTemplate.execute(new TransactionCallbackWithoutResult() {
    protected void doInTransactionWithoutResult(TransactionStatus status) {
        updateOperation1();
        updateOperation2();
    }
});
```
- 回滚
```java
transactionTemplate.execute(new TransactionCallbackWithoutResult() {

    protected void doInTransactionWithoutResult(TransactionStatus status) {
        try {
            updateOperation1();
            updateOperation2();
        } catch (SomeBusinessExeption ex) {
            status.setRollbackOnly();
        }
    }
});
```

#### 特殊事务配置
```java
public class SimpleService implements Service {

    private final TransactionTemplate transactionTemplate;

    public SimpleService(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);

        // the transaction settings can be set here explicitly if so desired
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        this.transactionTemplate.setTimeout(30); // 30 seconds
        // and so forth...
    }
}
```
#### 配置通用模板
```xml
<bean id="sharedTransactionTemplate"
        class="org.springframework.transaction.support.TransactionTemplate">
    <property name="isolationLevelName" value="ISOLATION_READ_UNCOMMITTED"/>
    <property name="timeout" value="30"/>
</bean>
```

### 17.6.2 使用PlatformTransactionManager
```sh
DefaultTransactionDefinition def = new DefaultTransactionDefinition();
// explicitly setting the transaction name is something that can only be done programmatically
def.setName("SomeTxName");
def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

TransactionStatus status = txManager.getTransaction(def);
try {
    // execute your business logic here
}
catch (MyException ex) {
    txManager.rollback(status);
    throw ex;
}
txManager.commit(status);
```
## 17.7 选择编程式还是声明式事务管理
- 如果应用只有少量事务操作，可以考虑编程式事务管理，可以指定事务名称是编程式事务独有的
- 如果有大量事务操作，声明式事务更好，它保证事务操作脱离于业务逻辑，并且易于配置

## 17.8 事务绑定事件
- 使用`@TransactionalEventListener`绑定event listener
- 通过指定`phase`参数，指定绑定事务的阶段，
  可选参数有：`BEFORE_COMMIT`,`AFTER_COMMIT`(Default),`AFTER_ROLLBACK`,`AFTER_COMPETION`

