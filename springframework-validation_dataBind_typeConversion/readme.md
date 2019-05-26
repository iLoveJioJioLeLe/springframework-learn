# 9. Validation, Data Binding, and Type Conversion

# 9.1 Introduction
- Validator
- DataBinder
- BeanWrapper
- PropertyEditor



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

9.5.4 ConversionService API
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