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
import javax.inject.Provider;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * process step ,do it last.
 * export one class,make all provide class
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/12/4
 */
public class MyComponentStep implements BasicAnnotationProcessor.ProcessingStep {
    private Filer filer;

    private Messager messager;

    private Map<ParameterizedTypeName, List<ParameterizedTypeName>> dependencyMap;

    public MyComponentStep(Filer filer, Messager messager, Map<ParameterizedTypeName, List<ParameterizedTypeName>> dependencyMap) {
        this.messager = messager;
        this.filer = filer;
        this.dependencyMap = dependencyMap;
    }

    @Override
    public Set<? extends Class<? extends Annotation>> annotations() {
        Set<Class<? extends Annotation>> classSet = new HashSet<>();
        classSet.add(MyComponent.class);
        return classSet;
    }

    @Override
    public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
        Set<Element> elementSet = elementsByAnnotation.get(MyComponent.class);
        try {
            for (Element element : elementSet) {
                processComponent(element);
            }
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return Collections.emptySet();
    }

    private void processComponent(Element element) {
        TypeElement typeElement = (TypeElement) element;
        String className = "MyDagger_" + typeElement.getSimpleName().toString();
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addSuperinterface(TypeName.get(element.asType()));
        List<ParameterizedTypeName> startTypeName = new ArrayList<>(10);
        List<ExecutableElement> methodList = ElementFilter.methodsIn(element.getEnclosedElements());
        Map<TypeMirror, ParameterizedTypeName> typeParameterizedMap = new HashMap<>();
        for (ExecutableElement executableElement : methodList) {
            TypeMirror returnType = executableElement.getReturnType();
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(returnType));
            typeParameterizedMap.put(returnType, parameterizedTypeName);
            startTypeName.add(parameterizedTypeName);
        }
        Set<ParameterizedTypeName> fieldInitialList = new LinkedHashSet<>(dependencyMap.size());
        for (ParameterizedTypeName parameterizedTypeName : startTypeName) {
            ergodicDependency(parameterizedTypeName, fieldInitialList);
        }
        Map<ParameterizedTypeName, String> fieldNameMap = generateFieldMap(fieldInitialList);
        for (ParameterizedTypeName field : fieldInitialList) {
            typeSpecBuilder.addField(field, fieldNameMap.get(field), Modifier.PRIVATE);
        }

        MethodSpec.Builder constructBuilder = MethodSpec.constructorBuilder();
        for (ParameterizedTypeName field : fieldInitialList) {
            String fieldName = fieldNameMap.get(field);
            List<ParameterizedTypeName> dependencyList = dependencyMap.get(field);
            if (dependencyList == null || dependencyList.isEmpty()) {
                constructBuilder.addCode("this." + fieldName + "=" + fieldName + "_MyDagger_Factory.create();\n");
            } else {
                List<String> paramList = new ArrayList<>(dependencyList.size());
                for (ParameterizedTypeName parameterizedTypeName : dependencyList) {
                    paramList.add(fieldNameMap.get(parameterizedTypeName));
                }
                constructBuilder.addCode("this." + fieldName + "=" + fieldName + "_MyDagger_Factory.create(" + ListUtils.implode(paramList, ",") + ");\n");
            }
        }
        typeSpecBuilder.addMethod(constructBuilder.build());

        for (ExecutableElement method : methodList) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getSimpleName().toString());
            methodBuilder.returns(TypeName.get(method.getReturnType()));
            methodBuilder.addModifiers(Modifier.PUBLIC);
            methodBuilder.addCode("return " + fieldNameMap.get(typeParameterizedMap.get(method.getReturnType())) + ".get();\n");
            typeSpecBuilder.addMethod(methodBuilder.build());
        }

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

    private Map<ParameterizedTypeName, String> generateFieldMap(Set<ParameterizedTypeName> fieldInitialList) {
        Map<ParameterizedTypeName, String> fieldNameMap = new HashMap<>();
        for (ParameterizedTypeName field : fieldInitialList) {
            String firstClass = field.typeArguments.get(0).toString();
            String[] fieldNameArray = firstClass.split("\\.");
            String fieldName = fieldNameArray[fieldNameArray.length - 1];
            fieldNameMap.put(field, fieldName);
        }
        return fieldNameMap;
    }

    private void ergodicDependency(ParameterizedTypeName parameterizedTypeName, Set<ParameterizedTypeName> fieldInitialList) {
        List<ParameterizedTypeName> dependencyList = dependencyMap.get(parameterizedTypeName);
        if (dependencyList != null && !dependencyList.isEmpty()) {
            for (ParameterizedTypeName innerParameterizedTypeName : dependencyList) {
                if (fieldInitialList.contains(innerParameterizedTypeName)) {
                    continue;
                }
                ergodicDependency(innerParameterizedTypeName, fieldInitialList);
            }
        }
        fieldInitialList.add(parameterizedTypeName);
    }
}
