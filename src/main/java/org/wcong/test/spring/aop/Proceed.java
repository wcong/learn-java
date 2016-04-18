package org.wcong.test.spring.aop;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/18
 */
public interface Proceed {
	Object proceed(MethodInvocation methodInvocation) throws Throwable;
}
