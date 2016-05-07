### 前言
[上一篇文章](./8a48dcc07fb6)介绍了SpringMvc的Controller和RequestMapping，这里在介绍一下*HandlerAdapter*和*MessageConvert*。通过自定义注解来实现RequestBody和ResponseBody的encode和decode。
### RequestResponseBodyMethodProcessor
[上一篇文章](./8a48dcc07fb6)介绍了*RequestMapping*在*DispatcherServlet*的作用。*RequestMapping*定义了HttpRequest对应的处理方法，*HandlerAdapter*则是代理了*HandlerMapping*的方法，并围绕*HandlerMapping*实现一些嵌入操作。
这些操作就包括*ResolveArgument*和*HandleReturnValue*。
*RequestResponseBodyMethodProcessor*是一个典型的例子，这个类同时实现了*HandlerMethodArgumentResolver*，*HandlerMethodReturnValueHandler*
其中*HandlerMethodArgumentResolver*主要有两个方法
```
public interface HandlerMethodArgumentResolver {
	boolean supportsParameter(MethodParameter parameter);
	Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception;
}
```
*HandlerMethodReturnValueHandler*同样也有两个方法。
```
public interface HandlerMethodReturnValueHandler {
	boolean supportsReturnType(MethodParameter returnType);
	void handleReturnValue(Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception;
}
```
所以*RequestResponseBodyMethodProcessor*实现的方法，就可以看出来这个类，会处理被*@RequestBody*注解的参数，和*@ResponseBody*注解的返回值。
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
1. 自定义注解
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
2. 定义数据结构
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
4. 定义HandlerAdapter
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
5. MethodResolver
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
6. MessageConvert
```
    public static class DataMessageConvert extends AbstractGenericHttpMessageConverter<Object> {
        @Override
        public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
            return ((Class) type).isAssignableFrom(RequestData.class);
        }
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
7. 入口
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