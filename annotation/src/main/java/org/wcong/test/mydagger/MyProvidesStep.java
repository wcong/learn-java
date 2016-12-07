package org.wcong.test.mydagger;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.wcong.test.util.ElementUtils;
import org.wcong.test.util.ListUtils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/12/4
 */
public class MyProvidesStep implements BasicAnnotationProcessor.ProcessingStep {

	private Filer filer;

	private Messager messager;

	private Map<ParameterizedTypeName,List<ParameterizedTypeName>> dependencyMap;

	public MyProvidesStep(Filer filer, Messager messager,Map<ParameterizedTypeName,List<ParameterizedTypeName>> dependencyMap) {
		this.messager = messager;
		this.filer = filer;
		this.dependencyMap = dependencyMap;
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

		String className = element.getSimpleName() + "_MyDagger_Factory";
		ParameterizedTypeName superClass = ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(element.asType()));
		TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
		typeSpecBuilder.addSuperinterface(superClass);
		typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        List<TypeMirror> injectParams = getInjectConstructMethod(typeElement);
		Map<String,ParameterizedTypeName> typeNameMap = makeProvideParams(injectParams);
        if( !typeNameMap.isEmpty() ){
			List<ParameterizedTypeName> dependencyList = new ArrayList<>(typeNameMap.size());
            for( Map.Entry<String,ParameterizedTypeName> typeNameEntry : typeNameMap.entrySet() ){
				dependencyList.add(typeNameEntry.getValue());
                typeSpecBuilder.addField(typeNameEntry.getValue(),typeNameEntry.getKey(),Modifier.PRIVATE,Modifier.FINAL);
            }
			dependencyMap.put(superClass,dependencyList);
			typeSpecBuilder.addMethod(makeContructMethod(typeNameMap));
        }
		typeSpecBuilder.addMethod(makeGetMethod(typeElement,typeNameMap));
		typeSpecBuilder.addMethod(makeCreateMethod(className, superClass,typeNameMap));

		String packageName = ElementUtils.getPackageName(typeElement);
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

	private MethodSpec makeContructMethod(Map<String,ParameterizedTypeName> typeNameMap){
		MethodSpec.Builder contructMethod = MethodSpec.constructorBuilder();
		for( Map.Entry<String,ParameterizedTypeName> typeNameEntry : typeNameMap.entrySet() ){
			contructMethod.addParameter(typeNameEntry.getValue(),typeNameEntry.getKey());
			contructMethod.addCode("this."+typeNameEntry.getKey()+"="+typeNameEntry.getKey()+";\n");
		}
		return contructMethod.build();
	}

    private Map<String,ParameterizedTypeName> makeProvideParams(List<TypeMirror> injectParams){
        if( injectParams.isEmpty() ){
            return Collections.emptyMap();
        }
		Map<String,ParameterizedTypeName> map = new HashMap<>();
        for(TypeMirror typeMirror : injectParams){
			String[] splitArray = typeMirror.toString().split("\\.");
			map.put(splitArray[splitArray.length-1],ParameterizedTypeName.get(ClassName.get(Provider.class),TypeName.get(typeMirror)));
        }
        return map;
    }

	private  List<TypeMirror> getInjectConstructMethod(TypeElement typeElement){
		for(Element element : typeElement.getEnclosedElements() ){
            if( ! (element instanceof ExecutableElement)){
                continue;
            }
            ExecutableElement executableElement = (ExecutableElement) element;
            if( !"<init>".equals(executableElement.getSimpleName().toString()) ){
                continue;
            }
            Inject inject = executableElement.getAnnotation(Inject.class);
            if( inject == null ){
                continue;
            }
            List<? extends VariableElement> variableElementList = executableElement.getParameters();
            if( !variableElementList.isEmpty() ){
                List<TypeMirror> typeMirrors = new ArrayList<>(variableElementList.size());
                for( VariableElement variableElement : variableElementList ){
                    typeMirrors.add(variableElement.asType());
                }
                return typeMirrors;
            }
		}
        return Collections.emptyList();
	}

	private MethodSpec makeGetMethod(TypeElement typeElement,Map<String,ParameterizedTypeName> typeNameMap ) {
		MethodSpec.Builder getMethodSpecBuilder = MethodSpec.methodBuilder("get");
		getMethodSpecBuilder.returns(TypeName.get(typeElement.asType()));
		getMethodSpecBuilder.addModifiers(Modifier.PUBLIC);
        if( typeNameMap.isEmpty() ) {
            getMethodSpecBuilder.addCode("return new " + typeElement.getSimpleName() + "();\n");
        }else{
            StringBuilder construct = new StringBuilder();
            construct.append("return new ").append( typeElement.getSimpleName()).append("(");
            List<String> paramList = new ArrayList<>(typeNameMap.size());
            for( Map.Entry<String,ParameterizedTypeName> typeNameEntry  :typeNameMap.entrySet()){
                paramList.add(typeNameEntry.getKey()+".get()");
            }
            construct.append(ListUtils.implode(paramList,",")).append(");\n");
            getMethodSpecBuilder.addCode(construct.toString());
        }
		return getMethodSpecBuilder.build();
	}

	private MethodSpec makeCreateMethod(String className, ParameterizedTypeName superClass,Map<String,ParameterizedTypeName> typeNameMap ) {
		MethodSpec.Builder createMethodSpecBuilder = MethodSpec.methodBuilder("create");
		createMethodSpecBuilder.returns(superClass);
        if( !typeNameMap.isEmpty() ){
            for( Map.Entry<String,ParameterizedTypeName> typeNameEntry : typeNameMap.entrySet() ) {
                createMethodSpecBuilder.addParameter(typeNameEntry.getValue(),typeNameEntry.getKey());
            }
        }
		createMethodSpecBuilder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        if( typeNameMap.isEmpty() ) {
            createMethodSpecBuilder.addCode("return new " + className + "();\n");
        }else{
            createMethodSpecBuilder.addCode("return new "+className+"(");
			List<String> paramList = new ArrayList<>(typeNameMap.size());
            for( Map.Entry<String,ParameterizedTypeName> typeNameEntry:typeNameMap.entrySet() ){
				paramList.add(typeNameEntry.getKey());
            }
			createMethodSpecBuilder.addCode(ListUtils.implode(paramList,",")+");\n");
        }
		return createMethodSpecBuilder.build();
	}

}
