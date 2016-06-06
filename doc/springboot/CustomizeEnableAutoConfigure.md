### 前言
上面几篇文章介绍了Spring的一些原理，这里开始介绍一下SpringBoot的一些原理。SpringBoot集成了Web容器和SpringFramework。可以快速的实现Web服务。
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
*spring-boot*实现了集成的实现类，*spring-boot-autoconfigure*则是根据当前classpath的类来自定义注入不同的Bean。

*ConditionEvaluator
*ConfigurationClassParser
*ConfigurationClassPostProcessor
*ConfigurationClassBeanDefinitionReader
*ConfigurationPropertiesBindingPostProcessorRegistrar
*BeanDefinitionBuilder
*ImportRegistry

### EnableAutoConfiguration
先看一个基本的例子。
1. 先注入必要的依赖。
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
*EnableAutoConfigurationImportSelector*继承了*DeferredImportSelector*。这个类都去了*ClassPath*下面的*META-INF/spring.factories*文件
这个文件包含了一些bean的配置
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
...
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration
...
```
其中*EmbeddedServletContainerAutoConfiguration*会根据当前的存在的类的信息注入必要的类。
*spring-boot-starter-tomcat*注入了tomcat的依赖，*EmbeddedServletContainerAutoConfiguration*发现存在*Tomcat.class*就会注入*TomcatEmbeddedServletContainerFactory*来内置web容器。
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
所以自定义*EnableAutoConfiguration*就比较简单了。