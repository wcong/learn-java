package org.wcong.test.tomcat;

import org.apache.catalina.startup.Bootstrap;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/1/18
 */
public class TomcatListenerTest {

	public static void main(String[] args) throws Exception {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.init();
		bootstrap.start();
	}
}
