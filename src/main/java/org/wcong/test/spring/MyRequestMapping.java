package org.wcong.test.spring;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/28
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {

	String name() default "";

	String[] value() default {};

	RequestMethod[] method() default {};

	String[] params() default {};

	String[] headers() default {};

	String[] consumes() default {};

	String[] produces() default {};
}
