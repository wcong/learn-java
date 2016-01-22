package org.wcong.test.spring;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/1/21
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MyComponent {

	String value() default "";
}
