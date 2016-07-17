package org.wcong.test.springboot;

import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/7/17
 */
public class MyEmbeddedServletContainer implements EmbeddedServletContainer {

	private DispatcherServlet dispatcherServlet;

	public MyEmbeddedServletContainer(DispatcherServlet dispatcherServlet) {
		this.dispatcherServlet = dispatcherServlet;
	}

	public void start() throws EmbeddedServletContainerException {
	}

	public void stop() throws EmbeddedServletContainerException {

	}

	public int getPort() {
		return 80;
	}

	public class Server {

		public void start(){}

	}
}
