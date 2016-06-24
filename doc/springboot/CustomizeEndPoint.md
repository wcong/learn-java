### 前言
[上一篇文章](./5206f74a4406)介绍了SpringBoot的*PropertySourceLoader*，自定义了Json格式的配置文件加载。这里再介绍下*EndPoint*，并通过自定*EndPoint*来介绍实现原理。
### EndPoint
SpringBoot的*EndPoint*主要是用来监控应用服务的运行状况，并集成在Mvc中提供查看接口。内置的*EndPoint*比如*HealthEndPoint*会监控dist和db的状况，
MetricsEndPoint则会监控内存和gc的状况。
*Endpoint*的接口如下，其中*invoke()*是主要的方法，用于返回监控的内容，*isSensitive()*用于权限控制。
```
    public interface Endpoint<T> {
        String getId();
        boolean isEnabled();
        boolean isSensitive();
        T invoke();
    }
```
*EndPoint*的加载还是依靠*spring.factories*实现的。*spring-boot-actuator*包下的*META-INF/spring.factories*配置了*EndpointAutoConfiguration*。
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
...
org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration,\
...
```
*EndpointAutoConfiguration*就会注入必要的*EndPoint*。有些*EndPoint*需要外部的收集类，比如*TraceEndpoint*。
```
    @Bean
	@ConditionalOnMissingBean
	public TraceEndpoint traceEndpoint() {
		return new TraceEndpoint(this.traceRepository);
	}
```
*TraceEndpoint*会记录每次请求的Request和Response的状态，需要嵌入到Request的流程中，这里就主要用到了3个类。
1. *TraceRepository*用于保存和获取Request和Response的状态。
```
    public interface TraceRepository {
        List<Trace> findAll();
        void add(Map<String, Object> traceInfo);
    }
```
2. *WebRequestTraceFilter*用于嵌入web request，收集请求的状态并保存在*TraceRepository*中。
3. *TraceEndpoint*，*invoke()*方法直接调用*TraceRepository*保存的数据。
```
    public class TraceEndpoint extends AbstractEndpoint<List<Trace>> {
        private final TraceRepository repository;
        public TraceEndpoint(TraceRepository repository) {
            super("trace");
            Assert.notNull(repository, "Repository must not be null");
            this.repository = repository;
        }
        public List<Trace> invoke() {
            return this.repository.findAll();
        }
    }
```
*EndPoint*的Mvc接口主要是通过*EndpointWebMvcManagementContextConfiguration*实现的，这个类的配置也放在*spring.factories*中。
```
...
org.springframework.boot.actuate.autoconfigure.ManagementContextConfiguration=\
org.springframework.boot.actuate.autoconfigure.EndpointWebMvcManagementContextConfiguration,\
org.springframework.boot.actuate.autoconfigure.EndpointWebMvcHypermediaManagementContextConfiguration
```
*EndpointWebMvcManagementContextConfiguration*注入*EndpointHandlerMapping*来实现*EndPoint*的Mvc接口。
```
	@Bean
	@ConditionalOnMissingBean
	public EndpointHandlerMapping endpointHandlerMapping() {
		Set<? extends MvcEndpoint> endpoints = mvcEndpoints().getEndpoints();
		CorsConfiguration corsConfiguration = getCorsConfiguration(this.corsProperties);
		EndpointHandlerMapping mapping = new EndpointHandlerMapping(endpoints,corsConfiguration);
		boolean disabled = this.managementServerProperties.getPort() != null && this.managementServerProperties.getPort() == -1;
		mapping.setDisabled(disabled);
		if (!disabled) {
			mapping.setPrefix(this.managementServerProperties.getContextPath());
		}
		if (this.mappingCustomizers != null) {
			for (EndpointHandlerMappingCustomizer customizer : this.mappingCustomizers) {
				customizer.customize(mapping);
			}
		}
		return mapping;
	}
```

### 自定义EndPoint
自定义*EndPoint*也是类似的原理。这里自定义*EndPoint*实现应用内存的定时收集。
1. 收集内存，*MemStatus*是内存存储结构，*MemCollector*是内存的收集类，使用Spring内置的定时功能，每个5秒收集当前内存。
```
    public static class MemStatus {
        public MemStatus(Date date, Map<String, Object> status) {
            this.date = date;
            this.status = status;
        }
        private Date date;
        private Map<String, Object> status;
        public Date getDate() {
            return date;
        }
        public Map<String, Object> getStatus() {
            return status;
        }
    }
```
```
    public static class MemCollector {
        private int maxSize = 5;
        private List<MemStatus> status;
        public MemCollector(List<MemStatus> status) {
            this.status = status;
        }
        @Scheduled(cron = "0/5 * *  * * ? ")
        public void collect() {
            Runtime runtime = Runtime.getRuntime();
            Long maxMemory = runtime.maxMemory();
            Long totalMemory = runtime.totalMemory();
            Map<String, Object> memoryMap = new HashMap<String, Object>(2, 1);
            Date date = Calendar.getInstance().getTime();
            memoryMap.put("maxMemory", maxMemory);
            memoryMap.put("totalMemory", totalMemory);
            if (status.size() > maxSize) {
                status.remove(0);
                status.add(new MemStatus(date, memoryMap));
            } else {
                status.add(new MemStatus(date, memoryMap));
            }
        }
    }
```
2. 自定义EndPoint，*getId*是*EndPoint*的唯一标识，也是Mvc接口对外暴露的路径。*invoke*方法，取出*maxMemory*和*totalMemory*和对应的时间。
```
	public static class MyEndPoint implements Endpoint {
        private List<MemStatus> status;
        public MyEndPoint(List<MemStatus> status) {
            this.status = status;
        }
        public String getId() {
            return "my";
        }
        public boolean isEnabled() {
            return true;
        }
        public boolean isSensitive() {
            return false;
        }
        public Object invoke() {
            if (status == null || status.isEmpty()) {
                return "hello world";
            }
            Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
            for (MemStatus memStatus : status) {
                for (Map.Entry<String, Object> entry : memStatus.status.entrySet()) {
                    List<Map<String, Object>> collectList = result.get(entry.getKey());
                    if (collectList == null) {
                        collectList = new LinkedList<Map<String, Object>>();
                        result.put(entry.getKey(), collectList);
                    }
                    Map<String, Object> soloCollect = new HashMap<String, Object>();
                    soloCollect.put("date", memStatus.getDate());
                    soloCollect.put(entry.getKey(), entry.getValue());
                    collectList.add(soloCollect);
                }
            }
            return result;
        }
    }
```
3. AutoConfig，注入了*MyEndPoint*，和*MemCollector*。
```
	public static class EndPointAutoConfig {
        private List<MemStatus> status = new ArrayList<MemStatus>();
        @Bean
        public MyEndPoint myEndPoint() {
            return new MyEndPoint(status);
        }
        @Bean
        public MemCollector memCollector() {
            return new MemCollector(status);
        }
    }
```
4. 程序入口。
```
	@Configuration
	@EnableAutoConfiguration
	public class CustomizeEndPoint {

		public static void main(String[] args) {
			SpringApplication application = new SpringApplication(CustomizeEndPoint.class);
			application.run(args);
		}
	}
```

### 结语
*EndPoint*也是通过*spring.factories*实现扩展功能，注入了对应的Bean来实现应用监控的功能。
