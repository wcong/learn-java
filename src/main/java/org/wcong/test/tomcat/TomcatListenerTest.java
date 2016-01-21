package org.wcong.test.tomcat;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

		// important Fix startup sequence - required if you don't use web.xml.
		standardContext.addLifecycleListener(new Tomcat.FixContextListener());

		standardContext.addApplicationLifecycleListener(new ServletContextListenerTest());

		StandardWrapper standardWrapper = new StandardWrapper();
		standardWrapper.setName("standardWrapper");
		standardWrapper.setServletClass(ServletTest.class.getName());

		standardContext.addChild(standardWrapper);
		standardContext.addServletMapping("/","standardWrapper");
		tomcat.getService();
		tomcat.getHost().setAppBase(".");
		tomcat.getHost().addChild(standardContext);
		tomcat.start();
		tomcat.getServer().await();
	}

	public static class ServletContextListenerTest implements ServletContextListener {

		public void contextInitialized(ServletContextEvent sce) {
			System.out.println("contextInitialized " + sce);
		}

		public void contextDestroyed(ServletContextEvent sce) {
			System.out.println("contextDestroyed " + sce);
		}
	}

	public static class ServletTest extends HttpServlet {
		@Override
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			resp.getWriter().write("test");
			resp.flushBuffer();
		}

	}
}
