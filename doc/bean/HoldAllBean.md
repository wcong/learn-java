### class hold all bean

DefaultListableBeanFactory

### field hold all bean and bean name

private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);

private final List<String> beanDefinitionNames = new ArrayList<String>();