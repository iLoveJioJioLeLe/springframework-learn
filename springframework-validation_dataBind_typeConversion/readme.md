# 9. Validation, Data Binding, and Type Conversion

# 9.1 Introduction
- Validator
- DataBinder
- BeanWrapper
- PropertyEditor

BeanWrapper内部使用了两种机制：
1. PropertyEditor。PropertyEditor隶属于Java Bean规范。PropertyEditor只提供了String <-> Object的转换
2. ConversionService。Spring自3.0之后提供的替代PropertyEditor的机制，见9.5

DataBinder主要提供两个功能：
1. 利用BeanWrapper，给对象的属性设值
2. 在设值的同时做Validation

PropertyEditor:
1. 只有String<->Object转换机制
2. JavaBean规范
Converter:
1. 多种类型转换机制
Formatter：
1. 只有String<->Object转换机制
2. 支持通过注解转换

关于BeanWrapper、DataBinder、ConversionService、Formatter概念的解释
https://segmentfault.com/a/1190000008938863

# 9.2 Validation using Spring's Validator interface
```java
public interface Validator {
    /**
    * 当前validator是否支持当前class对象
    */
	boolean supports(Class<?> clazz);
	/**
	* target是被校验对象，校验对象结果注册到errors中
    */
	void validate(Object target, Errors errors);
}

```
- 普通对象校验
```java
public class Address {
    private String province;

    private String city;
    //...
```
```java
@Component
public class AddressValidator implements Validator {
    public boolean supports(Class<?> clazz) {
        return Address.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "province", "province.empty");
        ValidationUtils.rejectIfEmpty(errors, "city", "city.empty");
    }
}
```
- 复合对象校验
```java
public class Person {

    private String name;

    private int age;

    private Address address;
    // ...
```
```java
@Component
public class PersonValidator implements Validator {

    private final Validator addressValidator;

    public PersonValidator(AddressValidator addressValidator) {
        this.addressValidator = addressValidator;
    }

    public boolean supports(Class<?> clazz) {
        return Person.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
        Person p = (Person) target;
        if (p.getAge() < 0) {
            errors.reject("age", "age.min");
        } else if (p.getAge() > 100) {
            errors.reject("age", "age.max");
        }
        errors.pushNestedPath("address");
        ValidationUtils.invokeValidator(this.addressValidator, p.getAddress(), errors);
    }
}
```
```java
@ComponentScan("com.yy.springframework.validate")
public class Bootstrap {
    public static void main(String args[]){
        ApplicationContext context = new AnnotationConfigApplicationContext(Bootstrap.class);
        PersonValidator validator = context.getBean(PersonValidator.class);
        Person p = new Person();
        p.setName("");
        p.setAge(-2);
        Address address = new Address();
        p.setAddress(address);
        if (validator.supports(p.getClass())) {
            BindException errors = new BindException(p, "person");
            validator.validate(p, errors);
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                //  name cannot be empty
                //  0<=age<=100
                //  province can not be null
                //  city can not be null
            }
        }
    }
}
```

## 9.3 Resolving codes to error messages

- Errors由MessageCodesResolver接口处理，默认实现是DefaultMessageCodesResolver

## 9.4 BeanWrapper

- BeanWrapper可以get/set JavaBean的property，任意深度的nested property
- BeanWrapper一般不直接用于编码，一般用在DataBinder和BeanFactory中

# 9.4.1 Setting and getting basic and nested properties


| Expression | Explanation |
| - | - |
| name | getName() setName() |
| account.name | getAccount().getName() getAccount.setName() |
| account[2] | array或list对象，根据下标get set |
| account[COMPANYNAME] | map对象，根据key get set |

```java
public class Bootstrap {
    public static void main(String args[]){
        BeanWrapper person = new BeanWrapperImpl(new Person());
        person.setPropertyValue("name", "zhangsan");
        PropertyValue value = new PropertyValue("age", 1);
        person.setPropertyValue(value);
        System.out.println(person.getWrappedInstance());
        person.setPropertyValue("address", new Address());
        person.setPropertyValue("address.province", "shanghai");
        System.out.println(person.getWrappedInstance());
        System.out.println(person.getPropertyValue("address.province"));
    }
}
```
### 9.4.2 内置PropertyEditor实现
| Class | Explanation |
| ByteArrayPropertyEditor |  ... |
| ... | ... |


- Spring使用`ava.beans.PropertyEditorManager`设置搜索`propertyEditor`的路径，包含了`sun.bean.editors`中`PropertyEditor`的实现类。
- 自定义PropertyEditor
```java
public class Address {
    private String province;

    private String city;

    // getter... setter...
}
```
```java
public class Person {

    private String name;

    private int age;

    private Address address;
    // getter... setter...
}
```
```xml
    <bean id="person" class="com.yy.springframework.pojo.Person">
        <property name="address" value="shanghai-pd"/>
    </bean>
```
```java
public class AddressEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Address address = new Address();
        String province = text.split("-")[0];
        String city = text.split("-")[1];
        address.setProvince(province);
        address.setCity(city);
        setValue(address);
    }
}
```
```java
public class Bootstrap {
    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext("propertyeditor/application.xml");
        Person bean = context.getBean(Person.class);
        System.out.println(bean);//{"name":"null","age":0,"address":{"province":"shanghai","city":"pd"}}
    }
}
```
- 标准JavaBean会自动发现`PropertyEditor`，如果它们在同包中。（`BeanInfo`也是）
- 注册自定义PropertyEditors
    - 标准JavaBeans`PropertyEditor`查找技术（同包下BeanName对应BeanNameEditor）
    ```sh
    com
      chank
        pop
          Address
          AddressEditor // the PropertyEditor for the Address class
    ```
    - `ConfigurableBeanFactory.registerCustomEditor()`方法，
      注：因为生命周期的原因Bean属性注入发生在Aware接口之前，所以BeanFactoryAware接口设置自定义PropertyEditor是无效的，所以只能使用BeanFactoryPostProcessor
    ```java
    @Component
    public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            beanFactory.registerCustomEditor(Address.class, AddressEditor.class);
        }
    }
    ```
    - BeanFactoryPostProcessor的实现类`CustomEditorConfigurer`(官方建议)
    ```xml
        <bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
            <property name="customEditors">
                <map>
                    <entry key="com.yy.springframework.pojo.Address" value="com.yy.springframework.propertyeditor.AddressEditor"/>
                </map>
            </property>
        </bean>
    ```
    - 使用`PropertyEditorRegistrars`注册`PropertyEditor`，它与`PropertyEditorRegistry`协同工作（一个被BeanWrapper和DataBinder继承的接口）。
      它避免了在自定义编辑器上进行同步的需要，它为每个bean创建新的PropertyEditor实例。
      `PropertyEditorRegistrars`与`CustomEditorConfigurer`结合使用可以很容易地与`DataBinder`和SpringMVC控制器共享。
        ```java
        @Component
        public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar {
            public void registerCustomEditors(PropertyEditorRegistry registry) {
                registry.registerCustomEditor(Address.class, new AddressEditor());
            }
        }
        ```
        ```xml
        <context:component-scan base-package="com.yy.springframework.propertyeditor"/>
        <bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
            <property name="propertyEditorRegistrars">
                <list>
                    <ref bean="myPropertyEditorRegistrar"/>
                </list>
            </property>
        </bean>
        ```
    
## 9.5 Spring Type Conversion 类型转换
- Converter --- Object -> Object 转换
- ConverterFactory --- Object -> 多种同类型的Object 转换
- GenericConverter --- 多种Object -> 多种Object 转换
## 9.5.1 Converter SPI(Service Provider Interface)
```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {

    T convert(S source);
}
```
- 对于convert的入参确保非空
- 确保Converter是线程安全的
- convert会抛出异常
```java
public class StringToInteger implements Converter<String, Integer> {
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
```
- `core.convert.support`包中提供了很多便捷的Converter实现

### 9.5.2 ConverterFactory
```java
package org.springframework.core.convert.converter;

public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
```
- S是要被转换的类型，R是要转换后的类型，T是R的子类
- StringToEnum ConverterFactory例子
```java
package org.springframework.core.convert.support;

final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }

    private final class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

        private Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            return (T) Enum.valueOf(this.enumType, source.trim());
        }
    }
}
```


### 9.5.3 GenericConverter
```java
package org.springframework.core.convert.converter;

public interface GenericConverter {

    public Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
}
```
- 复杂类型转换，考虑GenericConverter
- 没有强类型签名
- 支持多个来源类型和多个目标类型之间的转换
```java

/**
 * 自定义GenericConverter
 */
public class MyGenericConverter implements GenericConverter {
    /**
     * 转换类型集合
     * @return
     */
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> set = new HashSet<ConvertiblePair>();
        set.add(new ConvertiblePair(Integer.class, Address.class));
        set.add(new ConvertiblePair(String.class, Address.class));
        return set;
    }

    /**
     * 类型转换
     * @param source        来源对象
     * @param sourceType    来源对象类型 {@link TypeDescriptor#forObject(Object)}
     * @param targetType    目标对象类型 {@link TypeDescriptor#forObject(Object)}
     * @return
     */
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Address address = null;
        if (sourceType.getType() == Integer.class) {
            Integer i = (Integer) source;
            if (i == 1) {
            } else if (i == 2) {
            }
        } else if (sourceType.getType() == String.class) {
            String i = (String) source;
            if (i.equals("1")) {
            } else if (i.equals("2")) {
            }
        }
        return address;
    }
}
```


#### ConditionalGenericConverter
- 根据TypeDescriptor情况判断是否可以转换，如目标字段上存在特定注释
```java
public interface ConditionalConverter {

	boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);

}
```
```java
public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {

}
```

### 9.5.4 ConversionService API
- ConversionService提供统一的类型转换接口，Converters通常通过这个门面接口执行
```java
package org.springframework.core.convert;

public interface ConversionService {

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    <T> T convert(Object source, Class<T> targetType);

    boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

}
```

- 大多数ConversionService实现都同时实现了ConverterRegistry，它提供了注册converters

### 9.5.5 配置ConversionService
- ConversionService是一个无状态对象，被设计在容器启动的时候实例化，被多个线程共享
- 容器会在需要类型转换时调用ConversionService，你也可以注入ConversionService直接调用
- 注册默认ConversionService配置
```xml
<bean id="conversionService"
    class="org.springframework.context.support.ConversionServiceFactoryBean"/>
```
- 默认ConversionService能够转换string,number,enum,collection,map等
- 使用自定义converters扩展或者重写默认converters，可以定义`converters`属性
```xml
<bean id="conversionService"
        class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <bean class="example.MyCustomConverter"/>
        </set>
    </property>
</bean>
```

### 9.5.6 编程式使用ConversionService
- 自动注入配置好的ConversionService
```java
@Service
public class MyService {

    @Autowired
    public MyService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void doIt() {
        this.conversionService.convert(...)
    }
}
```
- 集合类型转换
```java
DefaultConversionService cs = new DefaultConversionService();

List<Integer> input = ....
cs.convert(input,
    TypeDescriptor.forObject(input), // List<Integer> type descriptor
    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
```

## 9.6 Spring Field Formatting
- String <-> Object 转换
- A Spring Container uses this system to bind bean property values. In addition, both the Spring Expression Language (SpEL) and DataBinder use this system to bind field values. 


### 9.6.1 Formatter SPI
```java
package org.springframework.format;

public interface Formatter<T> extends Printer<T>, Parser<T> {
}
```
```java
public interface Printer<T> {

    String print(T fieldValue, Locale locale);
}
```
```java
import java.text.ParseException;

public interface Parser<T> {

    T parse(String clientValue, Locale locale) throws ParseException;
}
```
- Spring日期<->String转换
```java
package org.springframework.format.datetime;

public final class DateFormatter implements Formatter<Date> {

    private String pattern;

    public DateFormatter(String pattern) {
        this.pattern = pattern;
    }

    public String print(Date date, Locale locale) {
        if (date == null) {
            return "";
        }
        return getDateFormat(locale).format(date);
    }

    public Date parse(String formatted, Locale locale) throws ParseException {
        if (formatted.length() == 0) {
            return null;
        }
        return getDateFormat(locale).parse(formatted);
    }

    protected DateFormat getDateFormat(Locale locale) {
        DateFormat dateFormat = new SimpleDateFormat(this.pattern, locale);
        dateFormat.setLenient(false);
        return dateFormat;
    }
}
```

### 9.6.2 注解驱动Formatting
- 为formatter绑定注解，需要实现AnnotationFormatterFactory
```java
package org.springframework.format;
// A对应field注解类型
public interface AnnotationFormatterFactory<A extends Annotation> {

    // 对应field的类型限制
    Set<Class<?>> getFieldTypes();

    // 获取Printer
    Printer<?> getPrinter(A annotation, Class<?> fieldType);

    // 获取Parser
    Parser<?> getParser(A annotation, Class<?> fieldType);
}
```
- 例如
```java
public final class NumberFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<NumberFormat> {

    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(asList(new Class<?>[] {
            Short.class, Integer.class, Long.class, Float.class,
            Double.class, BigDecimal.class, BigInteger.class }));
    }

    public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<Number> configureFormatterFrom(NumberFormat annotation, Class<?> fieldType) {
        if (!annotation.pattern().isEmpty()) {
            return new NumberStyleFormatter(annotation.pattern());
        } else {
            Style style = annotation.style();
            if (style == Style.PERCENT) {
                return new PercentStyleFormatter();
            } else if (style == Style.CURRENCY) {
                return new CurrencyStyleFormatter();
            } else {
                return new NumberStyleFormatter();
            }
        }
    }
}
```
```java
public class MyModel {

    @NumberFormat(style=Style.CURRENCY)
    private BigDecimal decimal;
}
```

### 9.6.3 注册FormatterRegistry SPI
- 集中定义Formatting规则
- FormattingConversionService大多实现了FormatterRegistry
```java
package org.springframework.format;

public interface FormatterRegistry extends ConverterRegistry {

    void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser);

    void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter);

    void addFormatterForFieldType(Formatter<?> formatter);

    void addFormatterForAnnotation(AnnotationFormatterFactory<?, ?> factory);
}
```


### 9.6.4 FormatterRegistrar SPI
- 用于通过FormatterRegistry注册相关的converters和formatters
```java
package org.springframework.format;

public interface FormatterRegistrar {

    void registerFormatters(FormatterRegistry registry);
}
```


### 9.7 配置全局date&time format
- date和time fields没被@DateTimeFormat修饰，默认使用`DateFormat.SHORT`风格转换，可以通过定义自己的全局format来改变
- 通过java配置全局日期格式
```java
@Configuration
public class AppConfig {

    @Bean
    public FormattingConversionService conversionService() {

        // Use the DefaultFormattingConversionService but do not register defaults
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        // Ensure @NumberFormat is still supported
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        // Register date conversion with a specific global format
        DateFormatterRegistrar registrar = new DateFormatterRegistrar();
        registrar.setFormatter(new DateFormatter("yyyyMMdd"));
        registrar.registerFormatters(conversionService);

        return conversionService;
    }
}
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd>

    <bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
        <property name="registerDefaultFormatters" value="false" />
        <property name="formatters">
            <set>
                <bean class="org.springframework.format.number.NumberFormatAnnotationFormatterFactory" />
            </set>
        </property>
        <property name="formatterRegistrars">
            <set>
                <bean class="org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar">
                    <property name="dateFormatter">
                        <bean class="org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean">
                            <property name="pattern" value="yyyyMMdd"/>
                        </bean>
                    </property>
                </bean>
            </set>
        </property>
    </bean>
</beans>
```

## 9.8 Spring Validation
Spring3增强了对validation的支持
1. 全面支持JSR-303Bean Validation API
2. 通过编程方式，DataBinder可以同时验证和绑定对象
3. SpingMVC支持校验@Controller的入参

### 9.8.1 JSR-303 Bean Validation API 总览
JSR-303标准化了Java平台的验证约束声明和元数据。 
使用此API，您可以使用声明性验证约束来注释域模型属性，
并且运行时会强制执行它们。 
您可以利用许多内置约束。 
您还可以定义自己的自定义约束。
```java
public class PersonForm {

    @NotNull
    @Size(max=64)
    private String name;

    @Min(0)
    private int age;
}
```

#### 注入Validator

>LocalValidatorFactoryBean implements both javax.validation.ValidatorFactory 
and javax.validation.Validator, 
as well as Spring’s org.springframework.validation.Validator. 
You may inject a reference to either of these interfaces into beans that need to invoke validation logic.

```java
import javax.validation.Validator;

@Service
public class MyService {

    @Autowired
    private Validator validator;
```
```java
import org.springframework.validation.Validator;

@Service
public class MyService {

    @Autowired
    private Validator validator;
}
```

#### 配置自定义Constraints
```java
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MyConstraintValidator.class)
public @interface MyConstraint {
}
```
实现ConstraintValidator接口
```java
public interface ConstraintValidator<A extends Annotation, T> {

	void initialize(A constraintAnnotation);

	boolean isValid(T value, ConstraintValidatorContext context);
}
```
```java
import javax.validation.ConstraintValidator;

public class MyConstraintValidator implements ConstraintValidator {

    @Autowired
    private Foo aDependency;// 可以注入

    void initialize(A constraintAnnotation) {
        // ...
    }
    
    boolean isValid(T value, ConstraintValidatorContext context) {
        return true;
    }
}
```
- Example
```java
public class User {

    @MyConstraint
    private Long id;

    private String username;

    private String password;
```
```java
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MyConstraintValidator.class)
public @interface MyConstraint {
    String message() default "xxx can not be null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```
```java
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {
    public void initialize(MyConstraint annotation) {
        System.out.println(annotation.annotationType() + " ConstraintValidator init");
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            System.out.println(context.getDefaultConstraintMessageTemplate());
        }
        return value != null;
    }
}
```
```xml
<!-- 拦截@org.springframework.validation.annotation.Validated修饰的类方法 -->
<bean class="org.springframework.validation.beanvalidation.MethodValidationPostProcessor"/>
```
```java
@Validated
@Service
public class UserService {

    public User getUserById(@Valid User user) {
        return user;
    }
}
```
```java
@ComponentScan("com.yy.springframework.validate")
@ImportResource("classpath:validate/application.xml")
public class Bootstrap {
    public static void main(String args[]){
        ApplicationContext context = new AnnotationConfigApplicationContext(Bootstrap.class);
        UserService bean = context.getBean(UserService.class);
        User user = new User();
//        user.setId(1L);
        bean.getUserById(user);
    }
}
```

#### Spring-driven method validation
1. 通过aop拦截@Validated修饰的类的方法
```xml
<bean class="org.springframework.validation.beanvalidation.MethodValidationPostProcessor"/>
```
2. LocalValidatorFactoryBean

### 9.8.3 配置DataBinder
- DataBinder能够配置Validator，配置后validator可以被执行通过`binder.validate()`，所有校验错误会自动添加到binder的BindingResult中
- 编程式使用DataBinder
```java
        Person p2 = new Person();
        validator = context.getBean(PersonValidator.class);
        DataBinder binder = new DataBinder(p2);
        binder.setValidator(validator);
        // bind to the target object
        MutablePropertyValues propertyValue = new MutablePropertyValues();
        propertyValue.addPropertyValue("name", "1111");
        binder.bind(propertyValue);
        // validate the target object
        binder.validate();
        // get BindingResult that includes any validation errors
        BindingResult results = binder.getBindingResult();
        System.out.println(results.getAllErrors());
```
