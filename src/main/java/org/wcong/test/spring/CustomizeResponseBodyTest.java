package org.wcong.test.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wcong on 2016/5/6.
 */
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

    public static class ResponseData {
        private Map<String, String> data;

        public Map<String, String> getData() {
            return data;
        }

        public void setData(Map<String, String> data) {
            this.data = data;
        }
    }

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


    @Configuration
    public static class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {

        @Bean
        public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
            RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.requestMappingHandlerAdapter();
            List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
            converters.add(new MyMessageConvert());
            List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
            argumentResolvers.add(new MyResolver(converters));
            requestMappingHandlerAdapter.setCustomArgumentResolvers(argumentResolvers);
            List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
            returnValueHandlers.add(new MyResolver(converters));
            requestMappingHandlerAdapter.setCustomReturnValueHandlers(returnValueHandlers);
            return requestMappingHandlerAdapter;
        }
    }

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

            // Try even with null return value. ResponseBodyAdvice could get involved.
            writeWithMessageConverters(returnValue, returnType, webRequest);
        }
    }

    public static class MyMessageConvert extends AbstractGenericHttpMessageConverter<Object> {

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


}
