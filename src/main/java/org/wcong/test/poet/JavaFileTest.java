package org.wcong.test.poet;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcong on 2016/11/11.
 */
public class JavaFileTest {
	public static void main(String[] args) {
		TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("JavaFile");
		typeSpecBuilder.addAnnotation(makeAnnotationSpec());
		typeSpecBuilder.addField(makeFieldSpec());
		typeSpecBuilder.addMethods(makeMethodSpec());
		JavaFile.Builder javaFileBuilder = JavaFile.builder("org.wcong.test.poet", typeSpecBuilder.build());
		System.out.println(javaFileBuilder.build().toString());
	}

	static AnnotationSpec makeAnnotationSpec() {
		AnnotationSpec.Builder builder = AnnotationSpec.builder(ClassName.get("org.wcong.test.poet", "MyAnnotation"));
		CodeBlock.Builder codeBlockBuilder = CodeBlock.builder().add("$S", "world");
		builder.addMember("hello", codeBlockBuilder.build());
		return builder.build();
	}

	static FieldSpec makeFieldSpec() {
		FieldSpec.Builder fileSpecBuilder = FieldSpec.builder(String.class, "hello", Modifier.PRIVATE);
		fileSpecBuilder.initializer(CodeBlock.of("\"world\""));
		return fileSpecBuilder.build();
	}

	static List<MethodSpec> makeMethodSpec() {
		List<MethodSpec> methodSpecList = new ArrayList<MethodSpec>();

		MethodSpec.Builder getMethodSpecBuilder = MethodSpec.methodBuilder("getHello");
		getMethodSpecBuilder.addModifiers(Modifier.PUBLIC);
		getMethodSpecBuilder.returns(TypeName.get(String.class));
		getMethodSpecBuilder.addCode(CodeBlock.builder().add("return hello;").build());
		methodSpecList.add(getMethodSpecBuilder.build());

		MethodSpec.Builder setMethodSpecBuilder = MethodSpec.methodBuilder("setHello");
		setMethodSpecBuilder.addModifiers(Modifier.PUBLIC);
		setMethodSpecBuilder.returns(TypeName.VOID);
		ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(TypeName.get(String.class), "hello");
		setMethodSpecBuilder.addParameter(parameterBuilder.build());
		setMethodSpecBuilder.addCode(CodeBlock.builder().add("this.hello = hello;").build());
		methodSpecList.add(setMethodSpecBuilder.build());

		MethodSpec.Builder toStringBuilder = MethodSpec.methodBuilder("toString");
		toStringBuilder.addModifiers(Modifier.PUBLIC);
		toStringBuilder.returns(TypeName.get(String.class));
		CodeBlock.Builder toStringCodeBuilder = CodeBlock.builder();
		toStringCodeBuilder.beginControlFlow("if( hello != null )");
		toStringCodeBuilder.add(CodeBlock.of("return \"hello\"+hello;"));
		toStringCodeBuilder.endControlFlow();
		toStringBuilder.addCode(toStringCodeBuilder.build());
		methodSpecList.add(toStringBuilder.build());

		return methodSpecList;
	}
}
