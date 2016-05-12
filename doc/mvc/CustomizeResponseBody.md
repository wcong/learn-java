### 前言
[上一篇文章](./8a48dcc07fb6)介绍了*SpringMvc*的*RequestMappingHandlerMapping*，自定义了*Controller*和*RequestMapping*。
这里再介绍一下*HandlerAdapter*和*HttpMessageConverter*，并通过自定义注解来实现*RequestBody*和*ResponseBody*。*HttpMessageConverter*最常见的应用就是json的decode和encode。
### RequestBody和ResponseBody
[上一篇文章](./8a48dcc07fb6)介绍了*RequestMappingHandlerMapping*在*DispatcherServlet*的作用。
*RequestMappingHandlerMapping*扫描了*RequestMapping*注释的HttpRequest对应的处理方法，并通过实现*HandlerMapping*的接口代理对应的方法。
而*HandlerAdapter*则是封装了*HandlerMapping*的方法，并围绕*HandlerMapping*实现一些嵌入操作。
*RequestMappingHandlerAdapter*是其中一个典型的例子，这个类包含*HandlerMethodArgumentResolver*，*HandlerMethodReturnValueHandler*的一些实现类来处理*RequestMapping*的参数和返回值。
```
	private HandlerMethodArgumentResolverComposite argumentResolvers;
	private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
```
*RequestResponseBodyMethodProcessor*是一个典型的例子，这个类同时实现了*HandlerMethodArgumentResolver*，*HandlerMethodReturnValueHandler*。
其中*HandlerMethodArgumentResolver*接口有两个方法。
```
public interface HandlerMethodArgumentResolver {
	boolean supportsParameter(MethodParameter parameter);
	Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception;
}
```
*HandlerMethodReturnValueHandler*接口同样也有两个方法。
```
public interface HandlerMethodReturnValueHandler {
	boolean supportsReturnType(MethodParameter returnType);
	void handleReturnValue(Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception;
}
```
所以从*RequestResponseBodyMethodProcessor*实现的方法，就可以看出来这个类，会处理被*@RequestBody*注解的参数，和*@ResponseBody*注解的返回值。
```
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestBody.class);
	}
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null ||
				returnType.getMethodAnnotation(ResponseBody.class) != null);
	}
```
接下来就介绍一下自定义ResponseBody和RequestBody的使用方法。
### 自定义ResponseBody和RequestBody
1. 自定义注解，因为都是是附加的注解，就不需要再加上*@Component*的注解了。
```
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyResponseBody {
}
```
```
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestBody {
}
```
2. 定义数据结构，简便起见，这里只decode和encode特定的类。*RequestData*是*MyRequestBody*修饰的类，*ResponseData*是*@MyResponseBody*修饰的类。
```
    public static class RequestData {
        private Map<String, String> data;
        public Map<String, String> getData() {
            return data;
        }
        public void setData(Map<String, String> data) {
            this.data = data;
        }
        public String toString() {
            return "{\"data\":" + data + "}";
        }
    }
```
```
   public static class ResponseData {
       private Map<String, String> data;
       public Map<String, String> getData() {
           return data;
       }
       public void setData(Map<String, String> data) {
           this.data = data;
       }
   }
```
3. 定义controller
```
    @Controller
    public static class MyController {
        @RequestMapping
        @MyResponseBody
        public ResponseData index(@MyRequestBody RequestData requestData) {
            System.out.println(requestData);
            ResponseData responseData = new ResponseData();
            responseData.setData(requestData.getData());
            return responseData;
        }
    }
```
4. 注入HandlerAdapter，并加入了两个*MyResolver*，只不过一个是*setCustomArgumentResolvers*，另一个是*setCustomReturnValueHandlers*。
*MyResolver*都加入了*DataMessageConvert*，这个实现了*HttpMessageConverter*，稍后会介绍到。
```
    @Configuration
    public static class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {
        @Bean
        public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
            RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.requestMappingHandlerAdapter();
            List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
            converters.add(new DataMessageConvert());
            List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
            argumentResolvers.add(new MyResolver(converters));
            requestMappingHandlerAdapter.setCustomArgumentResolvers(argumentResolvers);
            List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
            returnValueHandlers.add(new MyResolver(converters));
            requestMappingHandlerAdapter.setCustomReturnValueHandlers(returnValueHandlers);
            return requestMappingHandlerAdapter;
        }
    }
```
5. 自定义MethodResolver，继承了*AbstractMessageConverterMethodProcessor*，并自定义了*supportsParameter*和*supportsReturnType*来加载自定义的注解。
*resolveArgument*，*handleReturnValue*，是沿用*RequestResponseBodyMethodProcessor*的方法，来调用*HttpMessageConverter*的处理方法。
```
    public static class MyResolver extends AbstractMessageConverterMethodProcessor {
        public MyResolver(List<HttpMessageConverter<?>> converters) {
            super(converters);
        }
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(MyRequestBody.class);
        }
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            Object arg = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
            String name = Conventions.getVariableNameForParameter(parameter);
            WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
            if (arg != null) {
                validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                }
            }
            mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
            return arg;
        }
        public boolean supportsReturnType(MethodParameter returnType) {
            return returnType.getMethodAnnotation(MyResponseBody.class) != null;
        }
        public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
            mavContainer.setRequestHandled(true);
            writeWithMessageConverters(returnValue, returnType, webRequest);
        }
    }
```
6. *HttpMessageConverter*是处理*RequestMapping*的注释的方法的参数和返回值的接口类。自定义*HttpMessageConverter*，继承了*AbstractGenericHttpMessageConverter*来实现一些公用的方法。
实现了*canRead*方法，只解码*RequestData*这个类，同样实现了*canWrite*了方法，只编码*ResponseData*这个类。
简便起见这里只编码和解码*Map<String, String>*，方法也很简单，key和value直接用','链接，不同的entry之间用';'连接。
```
    public static class DataMessageConvert extends AbstractGenericHttpMessageConverter<Object> {
        @Override
        public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
            return ((Class) type).isAssignableFrom(RequestData.class);
        }
        @Override
        public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
            return ((Class) type).isAssignableFrom(ResponseData.class);
        }
        public List<MediaType> getSupportedMediaTypes() {
            return Collections.singletonList(MediaType.ALL);
        }
        protected boolean supports(Class<?> clazz) {
            return clazz.isAssignableFrom(Map.class);
        }
        public Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            return readMap(inputMessage);
        }
        private Object readMap(HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            Charset cs = Charset.forName("UTF-8");
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = inputMessage.getBody();
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) != -1) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(length);
                byteBuffer.put(b, 0, length);
                byteBuffer.flip();
                stringBuilder.append(cs.decode(byteBuffer).array());
            }
            String[] list = stringBuilder.toString().split(";");
            Map<String, String> map = new HashMap<String, String>(list.length);
            for (String entry : list) {
                String[] keyValue = entry.split(",");
                map.put(keyValue[0], keyValue[1]);
            }
            RequestData requestData = new RequestData();
            requestData.setData(map);
            return requestData;
        }
        public void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
            StringBuilder stringBuilder = new StringBuilder();
            Map<String, String> map = ((ResponseData) o).getData();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuilder.append(entry.getKey()).append(",").append(entry.getValue()).append(";");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            outputMessage.getBody().write(stringBuilder.toString().getBytes());
        }
        public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            return readMap(inputMessage);
        }
    }
```
7. 程序入口，跟[上一篇文章](./8a48dcc07fb6)类似。
```
    @Configuration
    public class CustomizeResponseBodyTest {
        public static void main(String[] args) throws ServletException, IOException {
            MockServletContext mockServletContext = new MockServletContext();
            MockServletConfig mockServletConfig = new MockServletConfig(mockServletContext);
            AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
            annotationConfigWebApplicationContext.setServletConfig(mockServletConfig);
            annotationConfigWebApplicationContext.register(CustomizeResponseBodyTest.class);
            DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigWebApplicationContext);
            dispatcherServlet.init(mockServletConfig);
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
            request.addHeader("Accept", "application/json");
            request.setContent(("result,hello world;date," + Calendar.getInstance().getTimeInMillis()).getBytes());
            dispatcherServlet.service(request, response);
            System.out.println(new String(response.getContentAsByteArray()));
        }
    }
```
### 结语
这里主要介绍了*HandlerAdapter*和*HttpMessageConverter*。*HandlerAdapter*封装了*HandlerMapping*的方法，而*HttpMessageConverter*则是*HandlerAdapter*内部一个功能的实现。接下来会介绍更多关于Mvc的内容。