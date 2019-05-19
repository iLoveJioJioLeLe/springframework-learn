<span id="top"></span>
[8. Resources](#8)
- [8.1 Introduction]



# 8. Resources


## 8.1 Introduction
Java标准的java.net.URL类和标准的不同URL前缀的handlers不能满足底层资源处理。
例如，没有标准URL实现来获取classpath和ServletContext的资源。
虽然可以为专用的URL前缀注册新的处理程序（类似于http :)这样的前缀的现有处理程序，但这通常非常复杂，并且URL接口仍然缺少一些理想的功能，例如检查被指向的资源存在的方法。


## 8.2 Resource接口
Spring’s Resource interface is meant to be a more capable interface for abstracting access to low-level resources.
```java
public interface Resource extends InputStreamSource {

    boolean exists();

    boolean isOpen();

    URL getURL() throws IOException;

    File getFile() throws IOException;

    Resource createRelative(String relativePath) throws IOException;

    String getFilename();

    String getDescription();

}
```
```java
public interface InputStreamSource {

    InputStream getInputStream() throws IOException;

}
```
- getInputStream(): locates and opens the resource, returning an InputStream for reading from the resource. It is expected that each invocation returns a fresh InputStream. It is the responsibility of the caller to close the stream.
- exists(): returns a boolean indicating whether this resource actually exists in physical form.
- isOpen(): returns a boolean indicating whether this resource represents a handle with an open stream. If true, the InputStream cannot be read multiple times, and must be read once only and then closed to avoid resource leaks. Will be false for all usual resource implementations, with the exception of InputStreamResource.
- getDescription(): returns a description for this resource, to be used for error output when working with the resource. This is often the fully qualified file name or the actual URL of the resource.
- Other methods allow you to obtain an actual URL or File object representing the resource (if the underlying implementation is compatible, and supports that functionality).

## 8.3 内建Resource实现

### 8.3.1 UrlResource
- UrlResource包装了java.net.URL
- 所有URL都有标准的String代表，`file:`文件系统，`http:`Http协议，`ftp:`FTP
- PropertyEditor会根据字符串前缀创建不同类型的Resource，如果无法识别字符串前缀，将创建UrlResource

### 8.3.2 ClassPathResource
- 这个类代表了从classpath获取的资源
- 使用线程上下文ClassLoader、给定ClassLoader或给定class，来加载资源
- 对在文件系统里存在的classpath提供类似java.io.File的支持，但是不支持未解压凯的jar包里的classpath资源
- PropertyEditor识别`classpath:`为ClassPathResource

### 8.3.3 FileSystemResource
- 处理java.io.File，支持URL

### 8.3.4 ServletContextResource
- 这是ServletContext资源的Resource实现，用于解释相关Web应用程序根目录中的相对路径。
- 总是支持stream和URL，但是java.io.File的支持需要web容器解压包后，资源物理存在于文件系统中，依赖于Servlet容器

### 8.3.5 InputStreamResource
- 提供InputStream
- 只有在没有适用的特定Resource实现时才使用此选项
- 与其他Resource相比，这是一个已经打开的资源，因此isOpen()总是返回true
- 如果想要保存资源描述或要重复读取流，请不要使用它

### 8.3.6 ByteArrayResource
- 提供byte array，创建ByteArrayInputStream

## 8.4 ResourceLoader
- ResourceLoader接口用于获取Resource实例
```java
public interface ResourceLoader {

    Resource getResource(String location);

}
```

- 所有application context都实现了ResourceLoader接口，所以所有context都可以用来获取Resource实例

- 如果调用`context.getResource()`方法，但是指定路径没有一个特殊前缀，则会获得一个Resource类型对象匹配当前application context
`Resource template = ctx.getResource("some/resource/path/myTemplate.txt");`
- 如果指定路径有特殊前缀，如`classpath`、`file`、`http`等，则根据前缀获取对应的Resource对象
`Resource template = ctx.getResource("classpath:some/resource/path/myTemplate.txt");`

- 不同前缀对应的Resource
| Prefix | Example | Explanation |
| - | - | - |
| classpath: | `classpath:com/myapp/config.xml` | load from the classpath |
| file: | `file:///data/config.xml` | Load as a URL, from the filesystem |
| http: | `https://myserver/logo.png` | Load as a URL |
| (none) | `/data/config.xml` | Depends on the underlying ApplicationContext |


## 8.5 The ResourceLoaderAware interface

- ResourceLoaderAware接口提供了ResourceLoader的引用
- 尽管ApplicationContext实现了ResourceLoader接口，但是如果ResourceLoader能够满足需求，请使用ResourceLoaderAware，而不要使用ApplicationContextAware

```java
@Component
public class ResourceLoaderAware1 implements ResourceLoaderAware {

    private static ResourceLoader resourceLoader;


    public static Resource getResource(String path) {
        return resourceLoader.getResource(path);
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        ResourceLoaderAware1.resourceLoader = resourceLoader;
    }
}
```
## 8.6 Resource作为依赖
- Spring使用`PropertyEditor`转换String类型path为Resource对象
- 如果bean的property是Resource类型，可以配置字符串进行依赖注入
```xml
<bean id="myBean" class="...">
    <property name="template" value="some/resource/path/myTemplate.txt"/>
</bean>
```
```java
@Component
public class MyBean {
    @Value("/Users/yuyue/Desktop/test.txt")
    private Resource template;

    public void printTemplate() {
        System.out.println(template);
    }
}
```


## 8.7 Application contexts and Resource paths

### 8.7.1 构造application contexts
- 当application context构造时，通常会传入字符串或字符串数组作为资源路径的位置，
如下创建ClassPathXmlApplicationContext，使用的是ClassPathResource（根据context类型）
`ApplicationContext ctx = new ClassPathXmlApplicationContext("conf/appContext.xml");`
- 资源路径前缀会override默认类型的Resource
`ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:conf/appContext.xml");`
- 如上会从classpath加载资源，但是这个ApplicationContext对应的ResourceLoader还是FileSystemResourceLoader

#### 构造ClassPathXmlApplicationContext实例 - shortcuts
com/
    foo/
        services.xml
        daos.xml
        MessagerService.class
```java
ApplicationContext ctx = new ClassPathXmlApplicationContext(
    new String[] {"services.xml", "daos.xml"}, MessengerService.class);
```       
