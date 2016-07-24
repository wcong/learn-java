package org.wcong.test.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletSecurityElement;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * customize servlet container
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 16/7/24
 */
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

		private Map<String, String> classNameMap = new HashMap<String, String>();

		private Map<String, Servlet> servletMap = new HashMap<String, Servlet>();

		private Map<String, Class<? extends Servlet>> servletClassMap = new HashMap<String, Class<? extends Servlet>>();

		public Map<String, ServletRegistration> servletRegistrationMap = new HashMap<String, ServletRegistration>();

		public String getVirtualServerName() {
			return null;
		}

		@Override
		public JspConfigDescriptor getJspConfigDescriptor() {
			return null;
		}

		@Override
		public ServletRegistration.Dynamic addServlet(String servletName, String className) {
			classNameMap.put(servletName, className);
			MyServletRegistration myServletRegistration = new MyServletRegistration(this);
			servletRegistrationMap.put(servletName, myServletRegistration);
			return new MyDynamic(myServletRegistration);
		}

		@Override
		public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
			servletMap.put(servletName, servlet);
			MyServletRegistration myServletRegistration = new MyServletRegistration(this);
			servletRegistrationMap.put(servletName, myServletRegistration);
			return new MyDynamic(myServletRegistration);
		}

		@Override
		public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
			servletClassMap.put(servletName, servletClass);
			MyServletRegistration myServletRegistration = new MyServletRegistration(this);
			servletRegistrationMap.put(servletName, myServletRegistration);
			return new MyDynamic(myServletRegistration);
		}

		@Override
		public <T extends Servlet> T createServlet(Class<T> c) throws ServletException {
			try {
				return c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public ServletRegistration getServletRegistration(String servletName) {
			return servletRegistrationMap.get(servletName);
		}

		@Override
		public Map<String, ? extends ServletRegistration> getServletRegistrations() {
			return servletRegistrationMap;
		}

		@Override
		public FilterRegistration.Dynamic addFilter(String filterName, String className) {
			return null;
		}

		@Override
		public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
			return null;
		}

		@Override
		public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
			return null;
		}

		@Override
		public <T extends Filter> T createFilter(Class<T> c) throws ServletException {
			return null;
		}

		@Override
		public FilterRegistration getFilterRegistration(String filterName) {
			return null;
		}

		@Override
		public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
			return Collections.emptyMap();
		}

		@Override
		public void addListener(Class<? extends EventListener> listenerClass) {
			return;
		}

		@Override
		public void addListener(String className) {
			return;
		}

		@Override
		public <T extends EventListener> void addListener(T t) {
			return;
		}

		@Override
		public <T extends EventListener> T createListener(Class<T> c) throws ServletException {
			return null;
		}
	}

	public static class MyServletRegistration implements ServletRegistration {

		private MyMockServletContext myMockServletContext;

		private String runAsRole;

		private Set<String> mapping = new HashSet<String>();

		private int loadOnStart;

		public MyServletRegistration(MyMockServletContext myMockServletContext) {
			this.myMockServletContext = myMockServletContext;
		}

		public Set<String> addMapping(String... strings) {
			for (String string : strings) {
				mapping.add(string);
			}
			return mapping;
		}

		public Collection<String> getMappings() {
			return mapping;
		}

		public String getRunAsRole() {
			return runAsRole;
		}

		public String getName() {
			return myMockServletContext.getServletContextName();
		}

		public String getClassName() {
			return this.getName();
		}

		public boolean setInitParameter(String s, String s1) {
			return myMockServletContext.setInitParameter(s, s1);
		}

		public String getInitParameter(String s) {
			return myMockServletContext.getInitParameter(s);
		}

		public Set<String> setInitParameters(Map<String, String> map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				myMockServletContext.setInitParameter(entry.getKey(), entry.getValue());
			}
			return map.keySet();
		}

		public Map<String, String> getInitParameters() {
			Map<String, String> map = new HashMap<String, String>();
			Enumeration<String> names = myMockServletContext.getInitParameterNames();
			while (names.hasMoreElements()) {
				String key = names.nextElement();
				map.put(key, myMockServletContext.getInitParameter(key));
			}
			return map;
		}

		public void setLoadOnStartup(int i) {
			this.loadOnStart = i;
		}

		public void setRunAsRole(String s) {
			this.setRunAsRole(s);
		}
	}

	public static class MyDynamic implements ServletRegistration.Dynamic {

		private MyServletRegistration myServletRegistration;

		private MultipartConfigElement multipartConfigElement;

		private boolean asyncSupported;

		public MyDynamic(MyServletRegistration myServletRegistration) {
			this.myServletRegistration = myServletRegistration;
		}

		public void setLoadOnStartup(int i) {
			myServletRegistration.setLoadOnStartup(i);
		}

		public void setMultipartConfig(MultipartConfigElement multipartConfigElement) {
			this.multipartConfigElement = multipartConfigElement;
		}

		public void setRunAsRole(String s) {
			myServletRegistration.setRunAsRole(s);
		}

		public Set<String> setServletSecurity(ServletSecurityElement servletSecurityElement) {
			return null;
		}

		public void setAsyncSupported(boolean b) {
			this.asyncSupported = b;
		}

		public Set<String> addMapping(String... strings) {
			return myServletRegistration.addMapping(strings);
		}

		public Collection<String> getMappings() {
			return myServletRegistration.getMappings();
		}

		public String getRunAsRole() {
			return myServletRegistration.getRunAsRole();
		}

		public String getName() {
			return myServletRegistration.getName();
		}

		public String getClassName() {
			return this.getName();
		}

		public boolean setInitParameter(String s, String s1) {
			myServletRegistration.setInitParameter(s, s1);
			return true;
		}

		public String getInitParameter(String s) {
			return myServletRegistration.getInitParameter(s);
		}

		public Set<String> setInitParameters(Map<String, String> map) {
			return myServletRegistration.setInitParameters(map);
		}

		public Map<String, String> getInitParameters() {
			return myServletRegistration.getInitParameters();
		}
	}

}
