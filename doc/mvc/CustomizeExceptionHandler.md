### 前言
[上一篇文章](./4fa3006c066f)介绍了*HandlerAdapter*和*HttpMessageConverter*，这里介绍SpringWeb的另一个重要的接口*HandlerExceptionResolver*。 
并通过自定义注解*@MyControllerAdvice*和*@MyExceptionHandler*，来实现异常的拦截。
### ControllerAdvice和ExceptionHandler
*DispatcherServlet*会读取ApplicationContext中是实现了*HandlerExceptionResolver*的bean，并在出现异常时调用对应的方法处理异常。
入口时在*doDispatch*方法中，这里有两次try-catch，最内的异常捕获后交给了*processDispatchResult*来处理。
```
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		...
		try {
			ModelAndView mv = null;
			Exception dispatchException = null;
			try {
				...
			}
			catch (Exception ex) {
				dispatchException = ex;
			}
			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
		}
		catch (Exception ex) {
			triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
		}
		catch (Error err) {
			triggerAfterCompletionWithError(processedRequest, response, mappedHandler, err);
		}
		finally {
			...
		}
	}
```
*HandlerExceptionResolver*主要的一个实现*ExceptionHandlerExceptionResolver*，这个类扫面*ControllerAdvice*配合*ExceptionHandler*定义的类和方法来提供异常处理。
*ControllerAdvice*是围绕*Controller*提供嵌入操作，下面的类的注释介绍了它的作用。这里主要用到了*ExceptionHandler*。
```
It is typically used to define {@link ExceptionHandler @ExceptionHandler},
{@link InitBinder @InitBinder}, and {@link ModelAttribute @ModelAttribute}
methods that apply to all {@link RequestMapping @RequestMapping} methods
```
*ControllerAdviceBean*负责加载*@ControllerAdvice*注解的类，稍后会自定义这个类来实现自定义功能。
*ExceptionHandler*是注解在处理特定异常的方法上，由*ExceptionHandlerMethodResolver*来扫描加载的。
### 自定义ExceptionHandler
接下来就是自定义*@MyControllerAdvice*和*MyExceptionHandler*来实现异常拦截。完成的代码还是放在[Github](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/spring/CustomizeExceptionHandlerTest.java)上了。
1. 自定义注解，*MyControllerAdvice*加上*@Component*，可以被Spring记载扫描到。
```
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Component
    public @interface MyControllerAdvice {
        String[] value() default {};
    }
```
```
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface MyExceptionHandler {
        Class<? extends Throwable>[] value() default {};
    }
```
2. 定义Controller
```
    @Controller
    public static class MyController {
        @RequestMapping
        @ResponseBody
        public Object index() {
            throw new RuntimeException("hello world");
        }
    }
```
3. 定义Exception Handler，这里接收到异常后返回一个map。
```
    @MyControllerAdvice
    public static class MyExceptionHandlerClass {
        @MyExceptionHandler(RuntimeException.class)
        @ResponseBody
        public Map<String, String> handleException(RuntimeException runtimeException) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("msg", runtimeException.getMessage());
            return data;
        }
    }
```
4. 导出*MyExceptionHandlerExceptionResolver*，*WebMvcConfigurationSupport*最终导出的是*HandlerExceptionResolverComposite*。
这个类会集成配置的*HandlerExceptionResolver*。所以这里继承了*WebMvcConfigurationSupport*，
并在*HandlerExceptionResolver*列表中加入了自定义的*MyExceptionHandlerExceptionResolver*。
```
    @Configuration
    public static class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {
        public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
            MyExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new MyExceptionHandlerExceptionResolver();
            exceptionHandlerExceptionResolver.setContentNegotiationManager(mvcContentNegotiationManager());
            exceptionHandlerExceptionResolver.setMessageConverters(getMessageConverters());
            exceptionHandlerExceptionResolver.setApplicationContext(getApplicationContext());
            exceptionHandlerExceptionResolver.afterPropertiesSet();
            exceptionResolvers.add(exceptionHandlerExceptionResolver);
        }
    }
```
5. 自定义*ExceptionHandlerExceptionResolver*，继承了*ExceptionHandlerExceptionResolver*，重用了大部分逻辑，
同时为了加载自定义的注解，使用了*MyControllerAdviceBean*和*MyExceptionHandlerMethodResolver*两个新的类，用来加载自定义的注解，
并保存在*exceptionHandlerCache*和*exceptionHandlerAdviceCache*中。在使用的时候，在根据Exception的类型来取得对应的方法。
```
    public static class MyExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {
        private final Map<Class<?>, MyExceptionHandlerMethodResolver> exceptionHandlerCache = new ConcurrentHashMap<Class<?>, MyExceptionHandlerMethodResolver>(64);
        private final Map<MyControllerAdviceBean, MyExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = new LinkedHashMap<MyControllerAdviceBean, MyExceptionHandlerMethodResolver>();
        @Override
        public void afterPropertiesSet() {
            initExceptionHandlerAdviceCache();
            super.afterPropertiesSet();
        }
        private void initExceptionHandlerAdviceCache() {
            if (getApplicationContext() == null) {
                return;
            }
            List<MyControllerAdviceBean> adviceBeans = MyControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
            AnnotationAwareOrderComparator.sort(adviceBeans);
            for (MyControllerAdviceBean adviceBean : adviceBeans) {
                MyExceptionHandlerMethodResolver resolver = new MyExceptionHandlerMethodResolver(adviceBean.getBeanType());
                if (resolver.hasExceptionMappings()) {
                    this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
                }
            }
        }
        public ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
            Class<?> handlerType = (handlerMethod != null ? handlerMethod.getBeanType() : null);
            if (handlerMethod != null) {
                MyExceptionHandlerMethodResolver resolver = this.exceptionHandlerCache.get(handlerType);
                if (resolver == null) {
                    resolver = new MyExceptionHandlerMethodResolver(handlerType);
                    this.exceptionHandlerCache.put(handlerType, resolver);
                }
                Method method = resolver.resolveMethod(exception);
                if (method != null) {
                    return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
                }
            }
            for (Map.Entry<MyControllerAdviceBean, MyExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
                MyExceptionHandlerMethodResolver resolver = entry.getValue();
                Method method = resolver.resolveMethod(exception);
                if (method != null) {
                    return new ServletInvocableHandlerMethod(entry.getKey().resolveBean(), method);
                }
            }
            return null;
        }
    }
```
6. 自定义*ExceptionHandlerMethodResolver*，这里重用了*ExceptionHandlerMethodResolver*的大部分内容。只是替换了注解的类型。
```
    public static class MyExceptionHandlerMethodResolver {
        public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = new ReflectionUtils.MethodFilter() {
            public boolean matches(Method method) {
                return (AnnotationUtils.findAnnotation(method, MyExceptionHandler.class) != null);
            }
        };
        private static final Method NO_METHOD_FOUND = ClassUtils.getMethodIfAvailable(System.class, "currentTimeMillis");
        private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentHashMap<Class<? extends Throwable>, Method>(16);
        private final Map<Class<? extends Throwable>, Method> exceptionLookupCache = new ConcurrentHashMap<Class<? extends Throwable>, Method>(16);
        public MyExceptionHandlerMethodResolver(Class<?> handlerType) {
            for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
                for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
                    addExceptionMapping(exceptionType, method);
                }
            }
        }
        @SuppressWarnings("unchecked")
        private List<Class<? extends Throwable>> detectExceptionMappings(Method method) {
            List<Class<? extends Throwable>> result = new ArrayList<Class<? extends Throwable>>();
            detectAnnotationExceptionMappings(method, result);
            if (result.isEmpty()) {
                for (Class<?> paramType : method.getParameterTypes()) {
                    if (Throwable.class.isAssignableFrom(paramType)) {
                        result.add((Class<? extends Throwable>) paramType);
                    }
                }
            }
            Assert.notEmpty(result, "No exception types mapped to {" + method + "}");
            return result;
        }
        public void detectAnnotationExceptionMappings(Method method, List<Class<? extends Throwable>> result) {
            MyExceptionHandler annot = AnnotationUtils.findAnnotation(method, MyExceptionHandler.class);
            result.addAll(Arrays.asList(annot.value()));
        }
        private void addExceptionMapping(Class<? extends Throwable> exceptionType, Method method) {
            Method oldMethod = this.mappedMethods.put(exceptionType, method);
            if (oldMethod != null && !oldMethod.equals(method)) {
                throw new IllegalStateException("Ambiguous @ExceptionHandler method mapped for [" +
                        exceptionType + "]: {" + oldMethod + ", " + method + "}");
            }
        }
        public boolean hasExceptionMappings() {
            return !this.mappedMethods.isEmpty();
        }
        public Method resolveMethod(Exception exception) {
            return resolveMethodByExceptionType(exception.getClass());
        }
        public Method resolveMethodByExceptionType(Class<? extends Exception> exceptionType) {
            Method method = this.exceptionLookupCache.get(exceptionType);
            if (method == null) {
                method = getMappedMethod(exceptionType);
                this.exceptionLookupCache.put(exceptionType, (method != null ? method : NO_METHOD_FOUND));
            }
            return (method != NO_METHOD_FOUND ? method : null);
        }
        private Method getMappedMethod(Class<? extends Exception> exceptionType) {
            List<Class<? extends Throwable>> matches = new ArrayList<Class<? extends Throwable>>();
            for (Class<? extends Throwable> mappedException : this.mappedMethods.keySet()) {
                if (mappedException.isAssignableFrom(exceptionType)) {
                    matches.add(mappedException);
                }
            }
            if (!matches.isEmpty()) {
                Collections.sort(matches, new ExceptionDepthComparator(exceptionType));
                return this.mappedMethods.get(matches.get(0));
            } else {
                return null;
            }
        }
    }
```
7. 自定义*ControllerAdviceBean*，这里简化了*ControllerAdviceBean*的使用属性，本来还有package和类型的过滤。
```
    public static class MyControllerAdviceBean implements Ordered {
        private final Object bean;
        private final BeanFactory beanFactory;
        private final int order;
        private MyControllerAdviceBean(Object bean, BeanFactory beanFactory) {
            this.bean = bean;
            this.beanFactory = beanFactory;
            this.order = initOrderFromBean(bean);
        }
        public int getOrder() {
            return this.order;
        }
        public Class<?> getBeanType() {
            Class<?> clazz = (this.bean instanceof String ?
                    this.beanFactory.getType((String) this.bean) : this.bean.getClass());
            return ClassUtils.getUserClass(clazz);
        }
        public Object resolveBean() {
            return (this.bean instanceof String ? this.beanFactory.getBean((String) this.bean) : this.bean);
        }
        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof MyControllerAdviceBean)) {
                return false;
            }
            MyControllerAdviceBean otherAdvice = (MyControllerAdviceBean) other;
            return (this.bean.equals(otherAdvice.bean) && this.beanFactory == otherAdvice.beanFactory);
        }
        @Override
        public int hashCode() {
            return this.bean.hashCode();
        }
        @Override
        public String toString() {
            return this.bean.toString();
        }
        public static List<MyControllerAdviceBean> findAnnotatedBeans(ApplicationContext applicationContext) {
            List<MyControllerAdviceBean> beans = new ArrayList<MyControllerAdviceBean>();
            for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Object.class)) {
                if (applicationContext.findAnnotationOnBean(name, MyControllerAdvice.class) != null) {
                    beans.add(new MyControllerAdviceBean(name, applicationContext));
                }
            }
            return beans;
        }
        private static int initOrderFromBean(Object bean) {
            return (bean instanceof Ordered ? ((Ordered) bean).getOrder() : initOrderFromBeanType(bean.getClass()));
        }
        private static int initOrderFromBeanType(Class<?> beanType) {
            return OrderUtils.getOrder(beanType, Ordered.LOWEST_PRECEDENCE);
        }
    }
```
8. 程序的入口
```
    public static void main(String[] args) throws ServletException, IOException {
        MockServletContext mockServletContext = new MockServletContext();
        MockServletConfig mockServletConfig = new MockServletConfig(mockServletContext);
        AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
        annotationConfigWebApplicationContext.setServletConfig(mockServletConfig);
        annotationConfigWebApplicationContext.register(CustomizeExceptionHandlerTest.class);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigWebApplicationContext);
        dispatcherServlet.init(mockServletConfig);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        request.addHeader("Accept", "application/json");
        request.setContent(("result,hello world;date," + Calendar.getInstance().getTimeInMillis()).getBytes());
        dispatcherServlet.service(request, response);
        System.out.println(new String(response.getContentAsByteArray()));
    }
```
运行程序就会发现捕获了*RuntimeException*，返回了对应的map。
### 结语
这里主要介绍了*HandlerExceptionResolver*的使用和自定义，目前为止已经介绍了*HandlerMapping*，*HandlerAdapter*，
*HandlerExceptionResolver*三个主要的SpringWeb的内部实现的接口，已经涵盖了*DispatcherServlet*的基本功能内部实现。