### 前言
[上一篇文章](./303450b6b0eb)介绍了SpringMvc的*ControllerAdvice*和*ExceptionHandler*，
这里在介绍一下*ViewResolver*的使用，并介绍一下*HandlerMethodReturnValueHandler*和*ViewResolver*的关系。
### ViewResolver和HandlerMethodReturnValueHandler
[自定义ResponseBody](./4fa3006c066f)这篇文章介绍过*ResponseBody*的编码规则，*ViewResolver*和*ResponseBody*是明显互斥的。
这两个不同类型的返回值就是通过不同的*HandlerMethodReturnValueHandler*来是实现的。
先看*RequestResponseBodyMethodProcessor*，这里设置了*setRequestHandled*为*true*，然后通过*HttpMessageConverters*编码对应的model。
```
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
			throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
		mavContainer.setRequestHandled(true);
		// Try even with null return value. ResponseBodyAdvice could get involved.
		writeWithMessageConverters(returnValue, returnType, webRequest);
	}
```
再看*ViewNameMethodReturnValueHandler*，这里没有设置*setRequestHandled*，而是取出*CharSequence*类型的返回值，并赋值viewName。
```
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		if (returnValue instanceof CharSequence) {
			String viewName = returnValue.toString();
			mavContainer.setViewName(viewName);
			if (isRedirectViewName(viewName)) {
				mavContainer.setRedirectModelScenario(true);
			}
		}
		else if (returnValue != null){
			// should not happen
			throw new UnsupportedOperationException("Unexpected return type: " + returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
		}
	}
```
再看*RequestMappingHandlerAdapter*中使用方法，假如*mavContainer.isRequestHandled*是true直接返回null，就不会调用*ViewResolver*了。
假如是false，会取出*mavContainer.getViewName*进行模板的映射。
```
    private ModelAndView getModelAndView(ModelAndViewContainer mavContainer,
			ModelFactory modelFactory, NativeWebRequest webRequest) throws Exception {
		modelFactory.updateModel(webRequest, mavContainer);
		if (mavContainer.isRequestHandled()) {
			return null;
		}
		ModelMap model = mavContainer.getModel();
		ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model);
		if (!mavContainer.isViewReference()) {
			mav.setView((View) mavContainer.getView());
		}
		if (model instanceof RedirectAttributes) {
			Map<String, ?> flashAttributes = ((RedirectAttributes) model).getFlashAttributes();
			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
			RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
		}
		return mav;
	}
```
### 自定义ViewResolver
相比*ExceptionHandler*，自定义*ViewResolver*就比较简单了，只要注入一个*ViewResolver*的实现类就可以了。
不过为了介绍*ViewResolver*的原理，这里自定义了一个*HandlerMethodReturnValueHandler*来取代*ViewNameMethodReturnValueHandler*;
完整的代码还是在[Github](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/spring/CustomizeViewResolverTest.java)上了。
1. 定义Controller。ViewName自定义类来包装viewName。
```
    @Controller
    public static class ControllerClass {
        @RequestMapping
        public ViewName index(ModelMap modelMap) {
            modelMap.put("message", "hello world");
            ViewName viewName = new ViewName();
            viewName.setName("index");
            return viewName;
        }
        @RequestMapping("html")
        public ViewName htmlIndex(ModelMap modelMap) {
            modelMap.put("message", "hello world");
            ViewName viewName = new ViewName();
            viewName.setName("html");
            return viewName;
        }
    }
    public static class ViewName {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
```
2. 注入自定义的*ViewResolvers*和*HandlerMethodReturnValueHandler*
```
    @Configuration
    public static class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {
        public void configureViewResolvers(ViewResolverRegistry registry) {
            MyViewResolver myViewResolver = new MyViewResolver();
            registry.viewResolver(myViewResolver);
        }
        public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
            returnValueHandlers.add(new MyHandlerMethodReturnValueHandler());
        }
    }
```
3. 自定义*HandlerMethodReturnValueHandler*，指定只支持*ViewName*，处理时，取出来*ViewName*的*name*，设置到*ModelAndViewContainer*中。
```
    public static class MyHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
        public boolean supportsReturnType(MethodParameter returnType) {
            return returnType.getParameterType().isAssignableFrom(ViewName.class);
        }
        public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
            ViewName viewName = (ViewName) returnValue;
            mavContainer.setViewName(viewName.getName());
        }
    }
```
4. 自定义*ViewResolver*，定义了*MyView*和*MyHtmlView*一个处理*index*返回纯文字，另一个处理*html*返回html格式。
```
    public static class MyViewResolver implements ViewResolver {
        private View htmlView = new MyHtmlView();
        private View view = new MyView();
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            if (viewName.equals("index")) {
                return view;
            } else if (viewName.equals("html")) {
                return htmlView;
            }
            return null;
        }
    }
    public static class MyView implements View {
        public String getContentType() {
            return "text/html";
        }
        public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, ?> entry : model.entrySet()) {
                stringBuilder.append(entry.getKey()).append(":").append(entry.getValue());
            }
            response.getWriter().write(stringBuilder.toString());
        }
    }
    public static class MyHtmlView implements View {
        public String getContentType() {
            return "text/html";
        }
        public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            String head = "<html><head><title>Hello World</title></head><body><ul>";
            String tail = "</ul></body></html>";
            StringBuilder sb = new StringBuilder();
            sb.append(head);
            for (Map.Entry<String, ?> entry : model.entrySet()) {
                sb.append("<li>").append(entry.getKey()).append(":").append(entry.getValue()).append("</li>");
            }
            sb.append(tail);
            response.getWriter().write(sb.toString());
        }
    }
```
5. 程序入口，这里同时请求了*/*和*/html*分别用到了*MyView*和*MyHtmlView*。
```
    @Configuration
    public class CustomizeViewResolverTest {
        public static void main(String[] args) throws ServletException, IOException {
            MockServletContext mockServletContext = new MockServletContext();
            MockServletConfig mockServletConfig = new MockServletConfig(mockServletContext);
            AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
            annotationConfigWebApplicationContext.setServletConfig(mockServletConfig);
            annotationConfigWebApplicationContext.register(CustomizeViewResolverTest.class);
            DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigWebApplicationContext);
            dispatcherServlet.init(mockServletConfig);
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
            dispatcherServlet.service(request, response);
            System.out.println(new String(response.getContentAsByteArray()));
            MockHttpServletResponse htmlResponse = new MockHttpServletResponse();
            MockHttpServletRequest htmlRequest = new MockHttpServletRequest("GET", "/html");
            dispatcherServlet.service(htmlRequest, htmlResponse);
            System.out.println(new String(htmlResponse.getContentAsByteArray()));
        }
    }
```
### 结语
*ViewResolver*和*ResponseBody*都是用来处理*HttpResponseBody*的内容的，只不过处理的方式不同。
*ResponseBody*的编码方式是一样的，一般是处理JSON的编码。*ViewResolver*还可以一根据ViewName来路由到不用的*View*，每个*View*都可以自己的编码规则。