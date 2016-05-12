package org.wcong.test.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CustomizeExceptionHandlerTest
 * Created by wcong on 2016/5/16.
 */
@Configuration
public class CustomizeExceptionHandlerTest {

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

    @Controller
    public static class MyController {
        @RequestMapping
        @ResponseBody
        public Object index() {
            throw new RuntimeException("hello world");
        }
    }

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

}
