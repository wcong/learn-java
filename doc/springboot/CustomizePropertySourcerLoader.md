### 前言
[上一篇文章](./2a171fa80756)介绍了SpringBoot的*EnableAutoConfiguration*，并通过自定义注解来实现相同的功能。
这里再介绍一下SpringBoot的配置文件的加载机制，SpringBoot会默认加载ClassPath下的application.properties的文件，下面会介绍实现的原理，并通过自定义PropertySourceLoader来自定义配置加载。
### PropertySourceLoader
SpringBoot加载配置文件的入口是*ConfigFileApplicationListener*，这个类实现了*ApplicationListener*和*EnvironmentPostProcessor*两个接口。
*SpringApplication*在初始化的时候会加载*spring.factories*配置的*ApplicationListener*接口的实现类。
```
	private void initialize(Object[] sources) {
		if (sources != null && sources.length > 0) {
			this.sources.addAll(Arrays.asList(sources));
		}
		this.webEnvironment = deduceWebEnvironment();
		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		this.mainApplicationClass = deduceMainApplicationClass();
	}
```
*ConfigFileApplicationListener*包含了*PropertySourcesLoader*，这个类会从*spring.factories*加载*PropertySourceLoader*的实现类。
```
	public PropertySourcesLoader(MutablePropertySources propertySources) {
		Assert.notNull(propertySources, "PropertySources must not be null");
		this.propertySources = propertySources;
		this.loaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class,getClass().getClassLoader());
	}
```
*PropertySourceLoader*就是加载配置接口类。
```
    public interface PropertySourceLoader {
        String[] getFileExtensions();
        PropertySource<?> load(String name, Resource resource, String profile)
                throws IOException;
    }
```
*getFileExtensions()*是返回支持的文件扩展名，比如*PropertiesPropertySourceLoader*支持的扩展是*xml*和*properties*。
*ConfigFileApplicationListener*定义了默认的文件名*DEFAULT_NAMES="application"*，所以SpringBoot会根据文件名加扩展名来加载文件。
*load*方法会读取配置文件，并返回*PropertySource*，SpringBoot会从*PropertySource*读取配置项，合并到总的配置对象中。

### 自定义PropertySourceLoader
所以自定义*PropertySourceLoader*就需要实现接口类，并配置到*spring.factories*中。
SpringBoot没有加载json的配置文件，这里就自定义*JsonPropertySourceLoader*来实现json格式配置文件的加载。完整的代码放在[Github](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/springboot/CustomizePropertySourceLoader.java)
1. 定义JsonPropertySourceLoader，这里返回json的扩展名，通过SpringBoot内置的JsonParse，解析文件。
解析成map格式，然后根据json的层级结构，递归进去，拼接成完整的key。
```
    public class JsonPropertySourceLoader implements PropertySourceLoader {
        public String[] getFileExtensions() {
            return new String[]{"json"};
        }
        public PropertySource<?> load(String name, Resource resource, String profile) throws IOException {
            Map<String, Object> result = mapPropertySource(resource);
            return new MapPropertySource(name, result);
        }
        private Map<String, Object> mapPropertySource(Resource resource) throws IOException {
            if (resource == null) {
                return null;
            }
            Map<String, Object> result = new HashMap<String, Object>();
            JsonParser parser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = parser.parseMap(readFile(resource));
            nestMap("", result, map);
            return result;
        }
        private String readFile(Resource resource) throws IOException {
            InputStream inputStream = resource.getInputStream();
            List<Byte> byteList = new LinkedList<Byte>();
            byte[] readByte = new byte[1024];
            int length;
            while ((length = inputStream.read(readByte)) > 0) {
                for (int i = 0; i < length; i++) {
                    byteList.add(readByte[i]);
                }
            }
            byte[] allBytes = new byte[byteList.size()];
            int index = 0;
            for (Byte soloByte : byteList) {
                allBytes[index] = soloByte;
                index += 1;
            }
            return new String(allBytes);
        }
        private void nestMap(String prefix, Map<String, Object> result, Map<String, Object> map) {
            if (prefix.length() > 0) {
                prefix += ".";
            }
            for (Map.Entry entrySet : map.entrySet()) {
                if (entrySet.getValue() instanceof Map) {
                    nestMap(prefix + entrySet.getKey(), result, (Map<String, Object>) entrySet.getValue());
                } else {
                    result.put(prefix + entrySet.getKey().toString(), entrySet.getValue());
                }
            }
        }
    }
```
2. 配置文件，这里配置了customize.property.message和日志级别logging.level.root。
```
{
  "customize": {
    "property": {
      "message": "hello world"
    }
  },
  "logging": {
    "level": {
      "root": "ERROR"
    }
  }
}
```
3. 配置PropertySourceLoader，在工程下新建*/META-INF/spring.factories*文件，并配置*JsonPropertySourceLoader*。
```
org.springframework.boot.env.PropertySourceLoader=org.wcong.test.springboot.JsonPropertySourceLoader
```
4. main入口，取出*customize.property.message*并打印出来。
```
    @Configuration
    @EnableAutoConfiguration
    public class CustomizePropertySourceLoader {
        @Value("${customize.property.message}")
        private String message;
        public static void main(String[] args) {
            SpringApplication springApplication = new SpringApplication(CustomizePropertySourceLoader.class);
            springApplication.setWebEnvironment(false);
            ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
            CustomizePropertySourceLoader customizePropertySourceLoader = configurableApplicationContext.getBean(CustomizePropertySourceLoader.class);
            System.out.println(customizePropertySourceLoader.message);
        }
    }
```

### 结语
SpringBoot通过*spring.factories*实现了很好的扩展功能。自定义模块相关一般是通过实现对应的接口，并配置到文件中。后面会介绍更多关于SpringBoot的内容。