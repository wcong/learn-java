package org.wcong.test.util;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

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

    public static void writeFile(Filer filer, Messager messager, String className, String text, Element element) {
        try {
            JavaFileObject sourceFile = filer.createSourceFile(className, element);
            Writer writer = sourceFile.openWriter();
            try {
                writer.write(text);
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }
}
