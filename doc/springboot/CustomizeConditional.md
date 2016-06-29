###前言
[上一篇文章](./9fab4e81d7bb)介绍了*SpringBoot*的*Endpoint*，这里在介绍下*@Conditional*。
*SpringBoot*的AutoConfig内部大量使用了*@Conditional*，会根据运行环境来动态注入Bean。
这里介绍一些*@Conditional*的使用和原理，并自定义*@Conditional*来自定义功能。
### Conditional
*@Conditional*是*SpringFramework*的功能，*SpringBoot*在它的基础上定义了*@ConditionalOnClass*，*@ConditionalOnProperty*的一系列的注解来实现更丰富的内容。
观察*@ConditionalOnClass*会发现它注解了*@Conditional(OnClassCondition.class)*。
```
    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Conditional(OnClassCondition.class)
    public @interface ConditionalOnClass {
        Class<?>[] value() default {};
        String[] name() default {};
    }
```
*OnClassCondition*则继承了*SpringBootCondition*，实现了*Condition*接口。
```
    public interface Condition {
        boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
    }
```
查看*SpringFramework*的源码会发现加载使用这些注解的入口在*ConfigurationClassPostProcessor*中，这个实现了*BeanFactoryPostProcessor*接口，前面介绍过，会嵌入到Spring的加载过程。
这个类主要是从ApplicationContext中取出*Configuration*注解的类并解析其中的注解，包括 *@Conditional*，*@Import*和 *@Bean*等。
解析 *@Conditional* 逻辑在*ConfigurationClassParser*类中，这里面用到了 *ConditionEvaluator* 这个类。
```
	protected void processConfigurationClass(ConfigurationClass configClass) throws IOException {
		if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationPhase.PARSE_CONFIGURATION)) {
			return;
		}
		......
	}
```
*ConditionEvaluator*中的*shouldSkip*方法则使用了 *@Conditional*中设置的*Condition*类。
```
	public boolean shouldSkip(AnnotatedTypeMetadata metadata, ConfigurationPhase phase) {
		if (metadata == null || !metadata.isAnnotated(Conditional.class.getName())) {
			return false;
		}
		if (phase == null) {
			if (metadata instanceof AnnotationMetadata &&
					ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata) metadata)) {
				return shouldSkip(metadata, ConfigurationPhase.PARSE_CONFIGURATION);
			}
			return shouldSkip(metadata, ConfigurationPhase.REGISTER_BEAN);
		}
		List<Condition> conditions = new ArrayList<Condition>();
		for (String[] conditionClasses : getConditionClasses(metadata)) {
			for (String conditionClass : conditionClasses) {
				Condition condition = getCondition(conditionClass, this.context.getClassLoader());
				conditions.add(condition);
			}
		}
		AnnotationAwareOrderComparator.sort(conditions);
		for (Condition condition : conditions) {
			ConfigurationPhase requiredPhase = null;
			if (condition instanceof ConfigurationCondition) {
				requiredPhase = ((ConfigurationCondition) condition).getConfigurationPhase();
			}
			if (requiredPhase == null || requiredPhase == phase) {
				if (!condition.matches(this.context, metadata)) {
					return true;
				}
			}
		}
		return false;
	}
	private List<String[]> getConditionClasses(AnnotatedTypeMetadata metadata) {
    		MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(Conditional.class.getName(), true);
    		Object values = (attributes != null ? attributes.get("value") : null);
    		return (List<String[]>) (values != null ? values : Collections.emptyList());
    }
```
### 自定义Conditional
所以自定义*Conditional*就是通过自定义注解和Condition的实现类。
1. 定义*@ConditionalOnMyProperties*
```
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Conditional(OnMyPropertiesCondition.class)
    public @interface ConditionalOnMyProperties {
        String name();
    }
```
2. 定义*OnMyPropertiesCondition*，这里继承了*SpringBootCondition*重用了部分功能，然后再*getMatchOutcome*实现了自定义的功能。
```
    public class OnMyPropertiesCondition extends SpringBootCondition {
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Object propertiesName = metadata.getAnnotationAttributes(ConditionalOnMyProperties.class.getName()).get("name");
            if (propertiesName != null) {
                String value = context.getEnvironment().getProperty(propertiesName.toString());
                if (value != null) {
                    return new ConditionOutcome(true, "get properties");
                }
            }
            return new ConditionOutcome(false, "none get properties");
        }
    }
```
3. *ConditionalOnMyProperties*使用类，还要加上*Configuration*注解才能生效。
```
    @Configuration
    @ConditionalOnMyProperties(name = "message")
    public static class ConditionClass {
        @Bean
        public HelloWorld helloWorld() {
            return new HelloWorld();
        }
    }
    private static class HelloWorld {
        public void print() {
            System.out.println("hello world");
        }
    }
```
4. 入口类，这里运行两次SpringApplication，传入的参数不同，第一次去Bean会抛出Bean不存在的异常，第二次就会正常输出。
```
    @Configuration
    @EnableAutoConfiguration
    public class CustomizeConditional {
        public static void main(String[] args) {
            SpringApplication springApplication = new SpringApplication(CustomizeConditional.class);
            springApplication.setWebEnvironment(false);
            ConfigurableApplicationContext noneMessageConfigurableApplicationContext = springApplication.run("--logging.level.root=ERROR","--endpoints.enabled=false");
            try {
                noneMessageConfigurableApplicationContext.getBean(HelloWorld.class).print();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ConfigurableApplicationContext configurableApplicationContext = springApplication.run("--message=haha", "--logging.level.root=ERROR");
            configurableApplicationContext.getBean(HelloWorld.class).print();
        }
    }
```
### 结语

