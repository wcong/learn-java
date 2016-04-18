package org.wcong.test.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * before method
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/13
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MyBefore {
}
