package org.wcong.test.javaformat;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.InvalidInputException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * test for IScanner
 * Created by wcong on 2016/11/21.
 */
public class IScannerTest {
    public static void main(String[] args) throws IOException, InvalidInputException {
        IScanner scanner = ToolFactory.createScanner(true, true, true, "1.8");
        Path path = Paths.get("src/main/java/org/wcong/test/guice/GuiceTest.java");
        String text = new String(Files.readAllBytes(path));
        int textLength = text.length();
        scanner.setSource(text.toCharArray());
        while (scanner.getCurrentTokenEndPosition() < textLength - 1) {
            scanner.getNextToken();
            int charI0 = scanner.getCurrentTokenStartPosition();
            // Get string, possibly with Unicode escapes.
            String originalTokText = text.substring(charI0, scanner.getCurrentTokenEndPosition() + 1);
            System.out.println(originalTokText);
        }
    }
}
