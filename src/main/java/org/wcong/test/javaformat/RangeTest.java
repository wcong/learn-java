package org.wcong.test.javaformat;

import com.google.common.collect.Range;

/**
 * Created by wcong on 2016/11/21.
 */
public class RangeTest {

    public static void main(String[] args) {
        Range<String> stringRange = Range.open("a", "b");
        System.out.println(stringRange);
        System.out.println(stringRange.span(Range.open("c", "d")));
    }
}
