### 前言
上面几篇文章介绍了SpringFramework的一些原理，这里开始介绍一下[SpringBoot](https://github.com/spring-projects/spring-boot)，并通过自定义一些功能来介绍SpringBoot的原理。
SpringBoot在SpringFramework的基础上集成了Web容器，日志等功能，可以快速的实现Web服务。
先看SpringBoot必要的依赖。
```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
    </dependency>
```
*spring-boot*包是*SpringBoot*的核心实现类，这里实现了集成的实现类。
*spring-boot-autoconfigure*则是根据当前classpath的类来自定义注入不同的实现类。

### EnableAutoConfiguration
先看一个基本的例子，这个例子实现了一个简单的web服务。
1. 先引入必要的依赖，开启web服务需要引入对应的EmbedWeb容器。
```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
    </dependency>
```
2. 程序入口，这里使用了*EnableAutoConfiguration*注解。
```
    @Configuration
    @EnableAutoConfiguration
    public class Bootstrap {
        public static void main(String[] args) {
            SpringApplication springApplication = new SpringApplication(Bootstrap.class);
            springApplication.run(args);
        }
        @Controller
        public static class MyController {
            @RequestMapping
            @ResponseBody
            public Map index() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("hello", "world");
                return map;
            }
        }
    }
```
观察*EnableAutoConfiguration*可以发现，这里*Import*了*EnableAutoConfigurationImportSelector*。
*Import*主要是配合*Configuration*来使用的，用来导出更多的*Configuration*类，*ConfigurationClassPostProcessor*会读取*Import*的内容来实现具体的逻辑。
*EnableAutoConfigurationImportSelector*实现了*DeferredImportSelector*接口,并实现了*selectImports*接口，用来导出*Configuration*类。
```
    String[] selectImports(AnnotationMetadata importingClassMetadata);
```
导出的类是读取了*ClassPath*下面的*META-INF/spring.factories*文件，这个文件内容大致如下。
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
...
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration
...
```
其中*EmbeddedServletContainerAutoConfiguration*是实现web服务的主要的配置类。这个类会根据当前存在的类的信息注入必要的*EmbeddedServletContainerFactory*类。
*spring-boot-starter-tomcat*引入了tomcat的依赖，所以*EmbeddedServletContainerAutoConfiguration*发现存在*Tomcat.class*就会注入*TomcatEmbeddedServletContainerFactory*来内置web容器。
```
    @Configuration
	@ConditionalOnClass({ Servlet.class, Tomcat.class })
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedTomcat {
		@Bean
		public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
			return new TomcatEmbeddedServletContainerFactory();
		}
	}
```

### 自定义EnableAutoConfiguration
自定义*EnableAutoConfiguration*就比较简单了。完整的代码在[Github](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/springboot/CustomizeEnableAutoConfigure.java)上了。
1. 自定义*EnableAutoConfiguration*，这里*Import*了*MyEnableAutoConfigurationImport*。
```
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @AutoConfigurationPackage
    @Import(MyEnableAutoConfigurationImport.class)
    public @interface MyEnableAutoConfiguration {
    }
```
2. 自定义*EnableAutoConfigurationImport*，注入了*ClassLoader*，并调用*SpringFactoriesLoader.loadFactoryNames()*方法，导出*Configuration*的类。
```
    public class MyEnableAutoConfigurationImport implements DeferredImportSelector, BeanClassLoaderAware {
        private ClassLoader classLoader;
        public void setBeanClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            List<String> beanNames = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, classLoader);
            return beanNames.toArray(new String[beanNames.size()]);
        }
    }
```
3. 入口类，这里使用了*MyEnableAutoConfiguration*注解。
```
    @Configuration
    @MyEnableAutoConfiguration
    public class CustomizeEnableAutoConfigure {
        public static void main(String[] args) {
            SpringApplication application = new SpringApplication(CustomizeEnableAutoConfigure.class);
            application.run(args);
        }
        @Controller
        public static class MyController {
            @RequestMapping
            @ResponseBody
            public Map index() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("hello", "world");
                return map;
            }
        }
    }
```
### 结语
这里主要介绍了*SpringBoot*的*EnableAutoConfiguration*的实现，后面会介绍更多关于*SpringBoot*的内容。