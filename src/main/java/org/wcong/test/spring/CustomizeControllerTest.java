package org.wcong.test.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/27
 */
@Configuration
public class CustomizeControllerTest {

	public static void main(String[] args) throws ServletException, IOException {
		AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
		MockServletContext mockServletContext = new MockServletContext();
		MockServletConfig mockServletConfig = new MockServletConfig(mockServletContext);
		annotationConfigWebApplicationContext.setServletConfig(mockServletConfig);
		annotationConfigWebApplicationContext.register(CustomizeControllerTest.class);

		DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigWebApplicationContext);
		dispatcherServlet.init(mockServletConfig);
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.addHeader("Accept","application/json");
		dispatcherServlet.service(request, response);
		System.out.println(new String(response.getContentAsByteArray()));
	}

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

}
