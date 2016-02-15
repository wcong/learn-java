#### Spring Bean Annotation

* org.springframework.stereotype.Component
* org.springframework.stereotype.Repository
* org.springframework.stereotype.Service
* org.springframework.stereotype.Controller

#### Basic Spring Bean Annotation

org.springframework.stereotype.Component is the root annotated Such classes are considered as candidates for auto-detection when using annotation-based configuration and classpath scanning

#### Customize Bean Annotation

So,customize annotation is easy

``` java

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MyComponent {
	String value() default "";
}

```

*value()* is importent for generate bean name
