package org.wcong.test.mydagger;

import javax.inject.Inject;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/12/4
 */
@MyProvides
public class MyProvidesTest {

    @Inject
    public MyProvidesTest(MyProvidesTest1 myProvidesTest1) {

    }

}
