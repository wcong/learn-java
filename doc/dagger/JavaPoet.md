### 前言
最近在用[dagger](https://github.com/google/dagger)开发应用，dagger是google在[square](https://github.com/square/dagger)的基础上去反射的依赖注入框架。
dagger会根据定义的注解在编译阶段自动生成依赖注入的代码，来减少运行期间反射的开销。
dagger依赖了JavaPoet和JavaFormat来实现编译代码，这里主要介绍下JavaPoet的内容和使用。

### JavaPoet
JavaPoet这样定义自己的项目。
```
Use beautiful Java code to generate beautiful Java code
```
所以JavaPoet定义了一系列类来尽可能优雅的描述java源文件的结构。观察JavaPoet的代码主要的接口可以分为以下几种：

* Spec 用来描述Java中基本的元素，包括类型，注解，字段，方法和参数
    * AnnotationSpec
    * FieldSpec
    * MethodSpec
    * ParameterSpec
    * TypeSpec
* Name 用来描述类型的引用，包括Void,和元素类型（int，long等）
    * TypeName
    * ArrayTypeName
    * ClassName
    * ParameterizedTypeName
    * TypeVariableName
    * WildcardTypeName
* CodeBlock 用来描述代码块的内容，包括普通的赋值，if判断，循环判断等
* JavaFile 完整的Java文件
* CodeWriter 读取JavaFile并转换成Java源文件

### 使用样例
通过下面的maven定义可以引用JavaPoet包。
```
    <dependency>
        <groupId>com.squareup</groupId>
        <artifactId>javapoet</artifactId>
        <version>1.7.0</version>
    </dependency>
```
这里使用JavaPoet定义了一个简单的Java类,完整的代码放在[Github]().

``` java
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
```