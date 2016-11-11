package org.wcong.test.oep;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

/**
 * Created by wcong on 2016/11/11.
 */
public class JavaFileTest {
    public static void main(String[] args) {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("JavaFile");
        JavaFile.Builder javaFileBuilder = JavaFile.builder("org.wcong.test.oep", typeSpecBuilder.build());
        System.out.println(javaFileBuilder.build().toString());
    }
}
