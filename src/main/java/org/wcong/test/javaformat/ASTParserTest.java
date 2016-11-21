package org.wcong.test.javaformat;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by wcong on 2016/11/21.
 */
public class ASTParserTest {
    public static void main(String[] args) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        Path path = Paths.get("src/main/java/org/wcong/test/guice/GuiceTest.java");
        String text = new String(Files.readAllBytes(path));
        parser.setSource(text.toCharArray());
        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        unit.getJavaElement();
    }
}
