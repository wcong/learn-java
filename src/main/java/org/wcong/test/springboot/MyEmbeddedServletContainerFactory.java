package org.wcong.test.springboot;

import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;

/**
 * my EmbeddedServletContainerFactory
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 16/7/17
 */
public class MyEmbeddedServletContainerFactory implements EmbeddedServletContainerFactory {
	public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers) {
		return null;
	}
}
