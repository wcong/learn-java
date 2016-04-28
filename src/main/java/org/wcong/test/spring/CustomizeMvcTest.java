package org.wcong.test.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/27
 */
@Configuration
@EnableWebMvc
public class CustomizeMvcTest {

	public static void main(String[] args) throws ServletException, IOException {
		AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
		MockServletContext mockServletContext = new MockServletContext();
		MockServletConfig mockServletConfig = new MockServletConfig(mockServletContext);
		annotationConfigWebApplicationContext.setServletConfig(mockServletConfig);
		annotationConfigWebApplicationContext.register(CustomizeMvcTest.class);

		DispatcherServlet dispatcherServlet = new DispatcherServlet(annotationConfigWebApplicationContext);
		dispatcherServlet.init(mockServletConfig);
		dispatcherServlet.refresh();
		MockHttpServletResponse response = new MockHttpServletResponse();
		dispatcherServlet.service(new MockHttpServletRequest("GET", "/"), response);
		System.out.println(new String(response.getContentAsByteArray()));
	}

	@Controller
	public static class IndexController {

		@RequestMapping("/")
		@ResponseBody
		public String index() {
			return "hello world";
		}

	}

}
