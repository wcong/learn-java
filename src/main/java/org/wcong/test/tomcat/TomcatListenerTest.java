package org.wcong.test.tomcat;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * StandardContext.applicationLifecycleListenersObjects
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 16/1/18
 */
public class TomcatListenerTest {

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();
		StandardContext standardContext = new StandardContext();
		standardContext.setName("test");
		standardContext.setPath("");
		standardContext.setDocBase("");
		standardContext.addWelcomeFile("welcome.txt");
		standardContext.addApplicationLifecycleListener(new ServletContextListenerTest());
		tomcat.getHost().setAppBase(".");
		tomcat.getHost().addChild(standardContext);
		tomcat.start();
		tomcat.getServer().await();
	}

	public static class ServletContextListenerTest implements ServletContextListener {

		public void contextInitialized(ServletContextEvent sce) {
			System.out.println("contextInitialized" + sce);
		}

		public void contextDestroyed(ServletContextEvent sce) {
			System.out.println("contextDestroyed" + sce);
		}
	}
}
