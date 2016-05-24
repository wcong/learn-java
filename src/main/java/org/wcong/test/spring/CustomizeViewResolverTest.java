package org.wcong.test.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/**
 * Created by wcong on 2016/5/19.
 */
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
        request.addHeader("Accept", "application/json");
        request.setContent(("result,hello world;date," + Calendar.getInstance().getTimeInMillis()).getBytes());
        dispatcherServlet.service(request, response);
        System.out.println(new String(response.getContentAsByteArray()));
    }

    @Controller
    public static class ControllerClass {
        @RequestMapping
        public String index(ModelMap modelMap) {
            modelMap.put("message", "hello world");
            return "index";
        }
    }

    @Configuration
    public static class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {

        public void configureViewResolvers(ViewResolverRegistry registry) {
            MyViewResolver myViewResolver = new MyViewResolver();
            registry.viewResolver(myViewResolver);
        }

    }

    public static class MyViewResolver implements ViewResolver {

        public static View view = new MyView();

        public View resolveViewName(String viewName, Locale locale) throws Exception {
            return view;
        }
    }

    public static class MyView implements View {

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
}
