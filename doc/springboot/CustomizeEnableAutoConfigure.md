### 前言
上面几篇文章介绍了SpringFramework的一些原理，这里开始介绍一下[SpringBoot](https://github.com/spring-projects/spring-boot)的一些原理。
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
先看一个基本的例子。
1. 先引入必要的依赖。
```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
    </dependency>
```
2. *EnableAutoConfiguration*
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
这个例子里注入了*@EnableAutoConfiguration*。
观察*EnableAutoConfiguration*可以发现，这里*Import*了*EnableAutoConfigurationImportSelector*。
*EnableAutoConfigurationImportSelector*继承了*DeferredImportSelector*。这个类取了*ClassPath*下面的*META-INF/spring.factories*文件
这个文件配置了一些bean的定义。
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
...
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration
...
```
其中*EmbeddedServletContainerAutoConfiguration*会根据当前的存在的类的信息注入必要的*EmbeddedServletContainerFactory*类。
*spring-boot-starter-tomcat*引入了tomcat的依赖，*EmbeddedServletContainerAutoConfiguration*发现存在*Tomcat.class*就会注入*TomcatEmbeddedServletContainerFactory*来内置web容器。
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
所以自定义*EnableAutoConfiguration*就比较简单了。完整的代码在[Github](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/springboot/CustomizeEnableAutoConfigure.java)上了。
1. 自定义*EnableAutoConfiguration*，这里*Import*了*MyEnableAutoConfigurationImport*
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
2. 自定义*EnableAutoConfigurationImport*，注入了*ClassLoader*，并调用*SpringFactoriesLoader.loadFactoryNames()*方法，导出*AutoConfiguration*的类
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