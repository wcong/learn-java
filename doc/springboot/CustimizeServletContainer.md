### 前言
[上一篇文章](./1d0fb7cd8a26)介绍了*SpringBoot*的 *@Conditional*的使用和原理，这里在介绍一下*EmbeddedServletContainer*。
*EmbeddedServletContainer*是实现内置Web容器的重要实现，这里介绍原理并自定义实现相应的功能。
### EmbeddedServletContainer
*SpringBoot*的Web容器是通过*META-INF/spring.factories*注入了*EmbeddedServletContainerAutoConfiguration*。
以tomcat为例，这里判断如果存在*Tomcat*等类，就会注入*TomcatEmbeddedServletContainerFactory*，
```
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnWebApplication
@Import(EmbeddedServletContainerCustomizerBeanPostProcessorRegistrar.class)
public class EmbeddedServletContainerAutoConfiguration {
	@Configuration
	@ConditionalOnClass({ Servlet.class, Tomcat.class })
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedTomcat {
		@Bean
		public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
			return new TomcatEmbeddedServletContainerFactory();
		}
	}
}
```
*TomcatEmbeddedServletContainerFactory*实现了*EmbeddedServletContainerFactory*
```
public interface EmbeddedServletContainerFactory {
	EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers);
}
```
初始化*Tomcat*并根据*ServletContextInitializer*对servletContext进行初始化。
```
    public EmbeddedServletContainer getEmbeddedServletContainer(
			ServletContextInitializer... initializers) {
		Tomcat tomcat = new Tomcat();
		File baseDir = (this.baseDirectory != null ? this.baseDirectory
				: createTempDir("tomcat"));
		tomcat.setBaseDir(baseDir.getAbsolutePath());
		Connector connector = new Connector(this.protocol);
		tomcat.getService().addConnector(connector);
		customizeConnector(connector);
		tomcat.setConnector(connector);
		tomcat.getHost().setAutoDeploy(false);
		tomcat.getEngine().setBackgroundProcessorDelay(-1);
		for (Connector additionalConnector : this.additionalTomcatConnectors) {
			tomcat.getService().addConnector(additionalConnector);
		}
		prepareContext(tomcat.getHost(), initializers);
		return getTomcatEmbeddedServletContainer(tomcat);
	}
```
*EmbeddedServletContainer*是具体的Web容器，提供http的服务。
```
public interface EmbeddedServletContainer {
	void start() throws EmbeddedServletContainerException;
	void stop() throws EmbeddedServletContainerException;
	int getPort();
}
```


### 自定义*EmbeddedServletContainer*
所以自定义需要实现*EmbeddedServletContainer*和*EmbeddedServletContainerFactory*两个接口，
另外SpringWeb强依赖*ServletContext*所以必须自定义一个简单的*ServletContext*，完整的代码还是放在[Github](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/springboot/CustomizeServletContainer.java)上了

1. 自定义*EmbeddedServletContainer*，这里使用Java内置的*HttpServer*提供http服务。
接收到请求后转发给*DispatcherServlet*进行处理。然后把response转换成*HttpServer*的输出结果。
```
    public class MyEmbeddedServletContainer implements EmbeddedServletContainer {
        private DispatcherServlet dispatcherServlet;
        private HttpServer httpServer;
        private MyHttpHandler httpHandler;
        public MyEmbeddedServletContainer(DispatcherServlet dispatcherServlet) {
            this.dispatcherServlet = dispatcherServlet;
            this.httpHandler = new MyHttpHandler(dispatcherServlet);
        }
        public void start() throws EmbeddedServletContainerException {
            try {
                httpServer = HttpServer.create();
                httpServer.bind(new InetSocketAddress("127.0.0.1", getPort()), 0);
                httpServer.createContext("/", httpHandler);
                httpServer.start();
                System.out.println("start http server");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void stop() throws EmbeddedServletContainerException {
            httpServer.stop(1);
        }
        public int getPort() {
            return 8080;
        }
        public static class MyHttpHandler implements HttpHandler {
            private DispatcherServlet dispatcherServlet;
            public MyHttpHandler(DispatcherServlet dispatcherServlet) {
                this.dispatcherServlet = dispatcherServlet;
            }
            public void handle(HttpExchange httpExchange) throws IOException {
                MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
                httpServletRequest.setPathInfo(httpExchange.getRequestURI().getPath());
                httpServletRequest.setRequestURI(httpExchange.getRequestURI().toString());
                MockHttpServletRequest request = new MockHttpServletRequest(httpExchange.getRequestMethod(), httpExchange.getRequestURI().getPath());
                MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
                try {
                    dispatcherServlet.service(request, httpServletResponse);
                } catch (ServletException e) {
                    e.printStackTrace();
                }
                for (String name : httpServletResponse.getHeaderNames()) {
                    httpExchange.getResponseHeaders().put(name, httpServletResponse.getHeaders(name));
                }
                byte[] content = httpServletResponse.getContentAsByteArray();
                httpExchange.sendResponseHeaders(200, content.length);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(content);
                outputStream.close();
                httpExchange.close();
            }
        }
    }
```
2. 自定义*EmbeddedServletContainerFactory*，注入*DispatcherServlet*。
因为SpringWeb强依赖*ServletContext*，所以这里实现了*MyMockServletContext*继承了*MockServletContext*，
并简单实现了*MockServletContext*不支持的功能，具体代码太长了，就不贴了，可以看Github上的完整的代码。
```
    @Configuration
	public static class MyEmbeddedServletContainerFactory implements EmbeddedServletContainerFactory {
		@Resource
		private DispatcherServlet dispatcherServlet;
		public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) {
			ServletContext servletContext = new MyMockServletContext();
			MockServletConfig mockServletConfig = new MockServletConfig();
			for (ServletContextInitializer initializer : initializers) {
				try {
					initializer.onStartup(servletContext);
					dispatcherServlet.init(mockServletConfig);
				} catch (ServletException e) {
					e.printStackTrace();
				}
			}
			return new MyEmbeddedServletContainer(dispatcherServlet);
		}
	}
    public static class MyMockServletContext extends MockServletContext {
    ......
    }
```
3. 程序入口，这里简单定义了一个Controller。
```
    @Configuration
    @EnableAutoConfiguration
    public class CustomizeServletContainer {
        public static void main(String[] args) {
            SpringApplication springApplication = new SpringApplication(CustomizeServletContainer.class);
            springApplication.run(args);
        }
        @Controller
        public static class MyController {
            @RequestMapping("/")
            @ResponseBody
            public Map index() {
                Map<String, String> msg = new HashMap<String, String>();
                msg.put("hello", "world");
                return msg;
            }
        }
	}
```
运行程序，正常访问就可以输出hello world;
### 结语
Spring基本覆盖了主流的JavaServlet容器，自己实现就太过复杂了，就没法模拟完整的Servlet的功能，但基本的原理是一致的。