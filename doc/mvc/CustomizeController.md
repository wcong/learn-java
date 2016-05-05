### 前言
[上一篇](./5347a462b3a5)文章介绍了Spring的事务管理，接下来开始介绍Spring的web模块。首先是介绍一下SpringMvc的基础模块，自定义*Controller*，*RequestMapping*注解，来实现自定义加载。
### 自定义Controller
Spring开启Mvc的主要是通过*EnableWebMvc*注解，观察源码就会发现，这个注解注入了*DelegatingWebMvcConfiguration*这个类，它继承了*WebMvcConfigurationSupport*，注入了必要的Bean。
Spring嵌入web应用容器的入口类是*DispatcherServlet*，这个类会读取*WebApplicationContext*中的必要的bean的信息，来提供mvc的服务。这篇文章先介绍下*Controller**RequestMapping*的注入和使用。
完整的代码在[Github](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/spring/CustomizeControllerTest.java)上，这里介绍几个主要的类。
1. 先定义自己的注解，*MyController*加上了*Component*注解，这样可以被Spring识别加载。*MyRequestMapping*则完全复用*RequestMapping*的属性，因为是附加是属性，所以就不需要加上*Component*注解了。
```
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Component
    public @interface MyController {
	    String value() default "";
    }
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface MyRequestMapping {
        String name() default "";
	    String[] value() default {};
	    RequestMethod[] method() default {};
	    String[] params() default {};
	    String[] headers() default {};
	    String[] consumes() default {};
	    String[] produces() default {};
    }
```
2. 定义controller和RequestMapping。
```
	@MyController
	public static class IndexController {
		@MyRequestMapping("/")
		@ResponseBody
		public Map index() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("result", "hello world");
			return map;
		}
	}
```
3. 加载自定义的注解，这里继承自*RequestMappingHandlerMapping*重载了*isHandler*和*getMappingForMethod*方法来加载自定义的注解，并根据*MyRequestMapping*的属性来生成*RequestMappingInfo*。
```
	public static class MyRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
		@Override
		protected boolean isHandler(Class<?> beanType) {
			return ((AnnotationUtils.findAnnotation(beanType, MyController.class) != null) || (
					AnnotationUtils.findAnnotation(beanType, MyRequestMapping.class) != null));
		}
		private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
			MyRequestMapping requestMapping = AnnotatedElementUtils
					.findMergedAnnotation(element, MyRequestMapping.class);
			RequestCondition<?> condition = (element instanceof Class<?> ?
					getCustomTypeCondition((Class<?>) element) :
					getCustomMethodCondition((Method) element));
			if (requestMapping == null) {
				return null;
			}
			return RequestMappingInfo.paths(resolveEmbeddedValuesInPatterns(requestMapping.value()))
					.methods(requestMapping.method()).params(requestMapping.params()).headers(requestMapping.headers())
					.consumes(requestMapping.consumes()).produces(requestMapping.produces())
					.mappingName(requestMapping.name()).customCondition(condition).build();
		}
		@Override
		protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
			RequestMappingInfo info = createRequestMappingInfo(method);
			if (info != null) {
				RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
				if (typeInfo != null) {
					info = typeInfo.combine(info);
				}
			}
			return info;
		}
	}
```
这个类继承了*HandlerMapping*接口，观察*DispatcherServlet*的源码就会发现，*HandlerMapping*接受httpRequest并查找到对应的method。
这个类保存了*RequestMapping*的注解的方法，保存在MappingRegistry的*mappingLookup*和*urlLookup*中，
其中urlLookup是用于直接查找的*directPathMatches*，如果没有*directPathMatches*，在遍历*mappingLookup*，查找匹配的处理方法。
```
    private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap<T, HandlerMethod>();
    private final MultiValueMap<String, T> urlLookup = new LinkedMultiValueMap<String, T>();
```
```
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
		List<Match> matches = new ArrayList<Match>();
		List<T> directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath);
		if (directPathMatches != null) {
			addMatchingMappings(directPathMatches, matches, request);
		}
		if (matches.isEmpty()) {
			// No choice but to go through all mappings...
			addMatchingMappings(this.mappingRegistry.getMappings().keySet(), matches, request);
		}
		.....
	}
```
4. 注入*RequestMappingHandlerMapping*，这里继承了*WebMvcConfigurationSupport*，然后重载了*requestMappingHandlerMapping*的注入方法。
*RequestMappingHandlerMapping*的配置方法跟*WebMvcConfigurationSupport*一致。
```
	@Configuration
	public static class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {
		@Bean
		@Override
		public RequestMappingHandlerMapping requestMappingHandlerMapping() {
			MyRequestMappingHandlerMapping handlerMapping = new MyRequestMappingHandlerMapping();
			handlerMapping.setOrder(0);
			handlerMapping.setInterceptors(getInterceptors());
			handlerMapping.setContentNegotiationManager(mvcContentNegotiationManager());
			handlerMapping.setCorsConfigurations(getCorsConfigurations());
			PathMatchConfigurer configurer = getPathMatchConfigurer();
			if (configurer.isUseSuffixPatternMatch() != null) {
				handlerMapping.setUseSuffixPatternMatch(configurer.isUseSuffixPatternMatch());
			}
			if (configurer.isUseRegisteredSuffixPatternMatch() != null) {
				handlerMapping.setUseRegisteredSuffixPatternMatch(configurer.isUseRegisteredSuffixPatternMatch());
			}
			if (configurer.isUseTrailingSlashMatch() != null) {
				handlerMapping.setUseTrailingSlashMatch(configurer.isUseTrailingSlashMatch());
			}
			if (configurer.getPathMatcher() != null) {
				handlerMapping.setPathMatcher(configurer.getPathMatcher());
			}
			if (configurer.getUrlPathHelper() != null) {
				handlerMapping.setUrlPathHelper(configurer.getUrlPathHelper());
			}
			return handlerMapping;
		}
	}
```
5. *DispatcherServlet*是web请求的处理类，接收*WebApplicationContext*和*ServletConfig*进行必要参数的初始化，
*service*方法，是处理请求的入口，接受request和response参数。简便起见，这里不启动web容器，而是用MockRequest和MockResponse来模拟处理请求。
```
	@Configuration
    public class CustomizeControllerTest {
    	public static void main(String[] args) throws ServletException, IOException {
    	    // init WebApplicationContext
    		AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
    		MockServletContext mockServletContext = new MockServletContext();
    		MockServletConfig mockServletConfig = new MockServletConfig(mockServletContext);
    		annotationConfigWebApplicationContext.setServletConfig(mockServletConfig);
    		annotationConfigWebApplicationContext.register(CustomizeControllerTest.class);
    		// init and start DispatcherServlet
    		DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigWebApplicationContext);
    		dispatcherServlet.init(mockServletConfig);
    		MockHttpServletResponse response = new MockHttpServletResponse();
    		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
    		request.addHeader("Accept","application/json");
    		dispatcherServlet.service(request, response);
    		System.out.println(new String(response.getContentAsByteArray()));
    	}
    }
```

### 结语
SpringMvc集成了Spring web flow的各个功能，这里先介绍下Spring的Controller和RequestMapping的使用，接下来会介绍包括HandlerAdapter和MassageConverter等更多功能。