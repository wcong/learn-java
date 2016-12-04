package com.wcong.test.mydagger;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.inject.Provider;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/12/4
 */
public class MyProvidesStep implements BasicAnnotationProcessor.ProcessingStep {

	private Filer filer;

	private Messager messager;

	public MyProvidesStep(Filer filer, Messager messager) {
		this.messager = messager;
		this.filer = filer;
	}

	@Override
	public Set<? extends Class<? extends Annotation>> annotations() {
		Set<Class<? extends Annotation>> classSet = new HashSet<>();
		classSet.add(MyProvides.class);
		return classSet;
	}

	@Override
	public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
		Set<Element> provideElements = elementsByAnnotation.get(MyProvides.class);
		if (provideElements == null || provideElements.isEmpty()) {
			return Collections.emptySet();
		}
		for (Element element : provideElements) {
			processProvideElement(element);
		}
		return Collections.emptySet();
	}

	private void processProvideElement(Element element) {
		TypeElement typeElement = (TypeElement) element;
		String className = element.getSimpleName() + "_MyAutoValue_Factory";
		ParameterizedTypeName superClass = ParameterizedTypeName
				.get(ClassName.get(Provider.class), TypeName.get(element.asType()));
		TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
		typeSpecBuilder.addSuperinterface(superClass);
		typeSpecBuilder.addModifiers(Modifier.PUBLIC);
		typeSpecBuilder.addMethod(makeGetMethod(typeElement));
		typeSpecBuilder.addMethod(makeCreateMethod(className, superClass));

		String packageName = getPackageName(typeElement);
		JavaFile.Builder javaFileBuilder = JavaFile.builder(packageName, typeSpecBuilder.build());
		String text = javaFileBuilder.build().toString();

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

	private MethodSpec makeGetMethod(TypeElement typeElement) {
		MethodSpec.Builder getMethodSpecBuilder = MethodSpec.methodBuilder("get");
		getMethodSpecBuilder.returns(TypeName.get(typeElement.asType()));
		getMethodSpecBuilder.addModifiers(Modifier.PUBLIC);
		getMethodSpecBuilder.addCode("return new " + typeElement.getSimpleName() + "();\n");
		return getMethodSpecBuilder.build();
	}

	private MethodSpec makeCreateMethod(String className, ParameterizedTypeName superClass) {
		MethodSpec.Builder createMethodSpecBuilder = MethodSpec.methodBuilder("create");
		createMethodSpecBuilder.returns(superClass);
		createMethodSpecBuilder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
		createMethodSpecBuilder.addCode("return new " + className + "();\n");
		return createMethodSpecBuilder.build();
	}

	private String getPackageName(Element typeElement) {
		while (true) {
			Element enclosingElement = typeElement.getEnclosingElement();
			if (enclosingElement instanceof PackageElement) {
				return ((PackageElement) enclosingElement).getQualifiedName().toString();
			}
			typeElement = enclosingElement;
		}
	}
}
