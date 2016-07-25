package org.wcong.test.springboot;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/7/17
 */
public class MyEmbeddedServletContainer implements EmbeddedServletContainer {

	private DispatcherServlet dispatcherServlet;

	private HttpServer httpServer;

	private MyHttpHandler httpHandler;

	public MyEmbeddedServletContainer(DispatcherServlet dispatcherServlet) {
		this.dispatcherServlet = dispatcherServlet;
		this.httpHandler = new MyHttpHandler(dispatcherServlet);
	}

	public void start() throws EmbeddedServletContainerException {
		try {
			httpServer = HttpServer.create();
			httpServer.bind(new InetSocketAddress("127.0.0.1", getPort()), 0);
			httpServer.createContext("/", httpHandler);
			httpServer.start();
			System.out.println("start http server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() throws EmbeddedServletContainerException {
		httpServer.stop(1);
	}

	public int getPort() {
		return 8080;
	}

	public static class MyHttpHandler implements HttpHandler {

		private DispatcherServlet dispatcherServlet;

		public MyHttpHandler(DispatcherServlet dispatcherServlet) {
			this.dispatcherServlet = dispatcherServlet;
		}

		public void handle(HttpExchange httpExchange) throws IOException {
			System.out.println("get msg");
			System.out.println(httpExchange.getRequestHeaders());
			MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
			httpServletRequest.setPathInfo(httpExchange.getRequestURI().getPath());
			httpServletRequest.setRequestURI(httpExchange.getRequestURI().toString());
			MockHttpServletRequest request = new MockHttpServletRequest(httpExchange.getRequestMethod(), httpExchange.getRequestURI().getPath());
			MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
			try {
				dispatcherServlet.service(request, httpServletResponse);
			} catch (ServletException e) {
				e.printStackTrace();
			}
			for (String name : httpServletResponse.getHeaderNames()) {
				httpExchange.getResponseHeaders().put(name, httpServletResponse.getHeaders(name));
			}
			byte[] content = httpServletResponse.getContentAsByteArray();
			httpExchange.sendResponseHeaders(200, content.length);
			OutputStream outputStream = httpExchange.getResponseBody();
			outputStream.write(content);
			outputStream.close();
			httpExchange.close();
		}
	}
}
