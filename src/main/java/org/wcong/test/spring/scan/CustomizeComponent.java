package org.wcong.test.spring.scan;

import java.lang.annotation.*;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/1/22
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomizeComponent {
	String value() default "";
}
