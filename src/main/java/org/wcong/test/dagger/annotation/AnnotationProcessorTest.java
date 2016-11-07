package org.wcong.test.dagger.annotation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.util.Set;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/11/4
 */
public class AnnotationProcessorTest {
	public static void main(String[] args) {

	}

	@SupportedAnnotationTypes("org.wcong.test.dagger.annotation.MyAnnotation")
	static class MyProcessor extends AbstractProcessor {

		private Trees trees;

		public void init(ProcessingEnvironment processingEnv) {
			super.init(processingEnv);
			trees = Trees.instance(processingEnv);
		}

		@Override
		public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

			final TreePathScanner<Object, CompilationUnitTree> scanner = new TreePathScanner<Object, CompilationUnitTree>() {
				public Trees visitClass(final ClassTree classTree, final CompilationUnitTree unitTree) {
					if (unitTree instanceof JCTree.JCCompilationUnit) {
						final JCTree.JCCompilationUnit compilationUnit = (JCTree.JCCompilationUnit) unitTree;
						if (compilationUnit.sourcefile.getKind() == JavaFileObject.Kind.SOURCE) {
							compilationUnit.accept(new TreeTranslator() {
								public void visitVarDef(final JCTree.JCVariableDecl tree) {
									super.visitVarDef(tree);
									if ((tree.mods.flags & Flags.FINAL) == 0) {
										tree.mods.flags |= Flags.FINAL;

									}
								}
								public void visitClassDef(JCTree.JCClassDecl tree) {
									super.visitClassDef(tree);
									//tree.implementing = tree.implementing.add(new JCTree.JCClassDecl());
								}
							});
						}

					}
					return trees;

				}
			};
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MyAnnotation.class);
			for (Element element : elements) {
				final TreePath path = trees.getPath(element);
				scanner.scan(path, path.getCompilationUnit());
			}
			return true;
		}
	}

	@MyAnnotation
	static class ParseClass {

	}
}
