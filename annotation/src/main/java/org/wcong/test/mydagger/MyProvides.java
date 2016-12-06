package org.wcong.test.mydagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * my provides
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/12/4
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface MyProvides {
}
