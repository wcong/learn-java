### 前言
[上一篇文章](./76e9e3a8ec0f)介绍了JavaPoet的使用，这里在介绍一下[AutoValue](https://github.com/google/auto)的原理，并模仿自定义实现一个AutoValue。
AutoValue的是Google为了实现*ValueClass*设计的自动编译框架，具体的介绍可以参考Google的官方[说明](https://github.com/google/auto/blob/master/value/userguide/index.md)。
Dagger内部也大量使用了AutoValue的功能，来实现*ValueClass*。
### AutoValue
AutoValue嵌入到JavaClass的编译过程，读取被注解的类，来创建一个新的ValueClass。这里有一个完整使用的[例子](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/autovalue/AutoValueTest.java)。
这里主要介绍一下AutoValue的实现。
1. 定义注解*AutoValue*。
```
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface AutoValue {
    }
```
2. 注册*processor*，AutoValue的jar包中的*META-INF/services*路径里面包含文件*javax.annotation.processing.Processor*，文件里包含了注册的*processor*，换行分割。这里面注册了*AutoValueProcessor*。
3. *AutoValueProcessor*的*process*方法实现了主要的处理逻辑，读取注释的类的信息，构造新的类，并写入文件。*processType*方法是处理单个类的方法，主要的逻辑如下
```
	AutoValueTemplateVars vars = new AutoValueTemplateVars();
    vars.pkg = TypeSimplifier.packageNameOf(type);
    vars.origClass = TypeSimplifier.classNameOf(type);
    vars.simpleClassName = TypeSimplifier.simpleNameOf(vars.origClass);
    vars.subclass = TypeSimplifier.simpleNameOf(subclass);
    vars.finalSubclass = TypeSimplifier.simpleNameOf(finalSubclass);
    vars.isFinal = applicableExtensions.isEmpty();
    vars.types = processingEnv.getTypeUtils();
    determineObjectMethodsToGenerate(methods, vars);
    defineVarsForType(type, vars, toBuilderMethods, propertyMethods, builder);
    GwtCompatibility gwtCompatibility = new GwtCompatibility(type);
    vars.gwtCompatibleAnnotation = gwtCompatibility.gwtCompatibleAnnotationString();
    String text = vars.toText();
    text = Reformatter.fixup(text);
    writeSourceFile(subclass, text, type);
```
*AutoValueTemplateVars*保存了新的类的信息，并根据对应的模板生成源文件字符串.
```
    private void writeSourceFile(String className, String text, TypeElement originatingType) {
        try {
          JavaFileObject sourceFile =
              processingEnv.getFiler().createSourceFile(className, originatingType);
          Writer writer = sourceFile.openWriter();
          try {
            writer.write(text);
          } finally {
            writer.close();
          }
        } catch (IOException e) {
          processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
              "Could not write generated class " + className + ": " + e);
        }
    }
```
*writeSourceFile*则会根据原生api将源代码写入本地文件。

### MyAutoValue
所以自定义*AutoValue*也是类似的原理。这里构造*MyAutoValue*来读取注解的类，生成新的带有get，set和toString方法类。
因为*processor*的注册只能在jar中使用，不能跟源文件放在一起，所以这里新建了一个[工程](https://github.com/wcong/learn-java/tree/master/annotation)来实现*MyAutoValue*，使用方法在[这里](https://github.com/wcong/learn-java/blob/master/src/main/java/org/wcong/test/autovalue/MyAutoValueTest.java)。
1. 定义*MyAutoValue*。
```
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface MyAutoValue {
}
```
2. *MyAutoValueProcessor*。同样先在*resources/META-INF/services*下新建*javax.annotation.processing.Processor*，并注册*MyAutoValueProcessor*。
*MyAutoValueProcessor*继承了*AbstractProcessor*，并在*process*中实现了主要的逻辑。
```
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MyAutoValue.class);
        if (elements == null || elements.isEmpty()) {
            return true;
        }
        for (Element element : elements) {
            if (!(element instanceof TypeElement)) {
                continue;
            }
            try {
                processType(element);
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
            }
        }
        return true;
    }
```
这里去取了所有被*MyAutoValue*注释的类，并交给processType去处理。
```
    private void processType(Element element) {
        TypeElement typeElement = (TypeElement) element;
        String className = element.getSimpleName() + "_MyAutoValue";
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addAnnotation(makeAnnotationSpec());
        typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        String packageName = getPackageName(typeElement);
        try {
            makeFieldAndMethod(typeElement, typeSpecBuilder);
        } catch (ClassNotFoundException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        JavaFile.Builder javaFileBuilder = JavaFile.builder(packageName, typeSpecBuilder.build());
        String text = javaFileBuilder.build().toString();
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(className, element);
            Writer writer = sourceFile.openWriter();
            try {
                writer.write(text);
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }
```
*processType*会读取类的字段生成一个新的*_MyAutoValue的类，并根据原有类的字段生成get，set和toString方法，然后将新类写入到本地文件中。
```
    private void makeFieldAndMethod(Element element, TypeSpec.Builder typeSpecBuilder) throws ClassNotFoundException {
        List<VariableElement> elementList = ElementFilter.fieldsIn(element.getEnclosedElements());
        if (elementList == null || elementList.isEmpty()) {
            return;
        }
        List<String> fieldList = new ArrayList<>(elementList.size());
        for (VariableElement variableElement : elementList) {
            String fieldName = variableElement.getSimpleName().toString();
            fieldList.add(fieldName);
            TypeName typeName = TypeName.get(variableElement.asType());
            typeSpecBuilder.addField(makeFieldSpec(fieldName, typeName));
            typeSpecBuilder.addMethod(makeSetMethod(fieldName, typeName));
            typeSpecBuilder.addMethod(makeGetMethod(fieldName, typeName));
        }
        typeSpecBuilder.addMethod(makeToStringMethod(fieldList));
    }
```
*makeFieldAndMethod*就是具体的构造字段和方法的逻辑，内部使用JavaPoet实现的，可以参考完整代码和上一篇文章，这里就不列出了。
3. 打包编译，需要注意的*META-INF/services*的*javax.annotation.processing.Processor*会阻止javac的编译，打完包会发现里面没有class文件，所以需要加上特殊的参数。
```
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>2.3.2</version>
                <configuration>
                    <source>1.7</source>
                    <target>1.7</target>
                    <encoding>UTF-8</encoding>
                    <compilerArgument>-proc:none</compilerArgument>
                </configuration>
            </plugin>
        </plugins>
    </build>
```
加上*<compilerArgument>-proc:none</compilerArgument>*就可以实现完整的打包了。
4. 使用*MyAutoValue*。在*MyAutoValueClassTest*类上注解*MyAutoValue*。
```
    @MyAutoValue
    public class MyAutoValueClassTest {
        private String a;
        private String b;
        private int c;
    }
```
编译完后就会生成以下的新类，会发现自定带上了get，set和toString的方法。
```
public class MyAutoValueClassTest_MyAutoValue {
    private String a;
    private String b;
    private int c;
    public MyAutoValueClassTest_MyAutoValue() {
    }
    public void setA(String a) {
        this.a = a;
    }
    public String getA() {
        return this.a;
    }
    public void setB(String b) {
        this.b = b;
    }
    public String getB() {
        return this.b;
    }
    public void setC(int c) {
        this.c = c;
    }
    public int getC() {
        return this.c;
    }
    public String toString() {
        return "{\"a\":\"" + this.a + "\",\"b\":\"" + this.b + "\",\"c\":\"" + this.c + "\"}";
    }
}
```

### 结语
dagger的实现跟AutoValue类似，也是根据注解嵌入编译实现新的类，只是AutoValue的逻辑比较简单，只是实现ValueClass的构造，dagger会涉及到更多依赖注入的功能。后面会介绍更多dagger的内容。