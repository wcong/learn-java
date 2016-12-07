package org.wcong.test.util;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;

/**
 * Created by wcong on 2016/12/6.
 */
public class ElementUtils {
    public static String getPackageName(Element element) {
        while (true) {
            Element enclosingElement = element.getEnclosingElement();
            if (enclosingElement instanceof PackageElement) {
                return ((PackageElement) enclosingElement).getQualifiedName().toString();
            }
            element = enclosingElement;
        }
    }
}
