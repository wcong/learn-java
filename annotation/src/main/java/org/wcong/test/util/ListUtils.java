package org.wcong.test.util;

import java.util.Iterator;
import java.util.List;

/**
 * Created by wcong on 2016/12/6.
 */
public class ListUtils {

    public static <T> String implode(List<T> list, String joint) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        Iterator<T> iterator = list.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iterator.next());
        while (iterator.hasNext()) {
            sb.append(joint).append(iterator.next());
        }
        return sb.toString();
    }

}
