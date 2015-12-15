### how spring resolve dependency for a bean

``` java
org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency()
```

#### detail 
judge type in (String,array,Collection,Map,etc)

#### Collection

usage of generic type reflection

``` java
ParameterizedType
``` 

