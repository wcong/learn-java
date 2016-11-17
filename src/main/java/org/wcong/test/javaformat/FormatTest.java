package org.wcong.test.javaformat;

import com.google.googlejavaformat.java.Main;

/**
 * Created by wcong on 2016/11/17.
 */
public class FormatTest {

    public static void main(String[] args) {
        String[] files = new String[1];
        files[0] = "src/main/java/org/wcong/test/guice/GuiceTest.java";
        Main.main(files);
    }
}
