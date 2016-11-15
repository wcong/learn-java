### 前言
最近在用[dagger](https://github.com/google/dagger)开发应用，dagger是google在[square](https://github.com/square/dagger)的基础上去反射的依赖注入框架。
dagger会根据定义的直接在编译阶段自动生成依赖注入的代码，来减少运行期间反射的开销。
dagger依赖了square的JavaPoet和JavaFormat来实现编译代码。这里主要介绍下JavaPoet。

### JavaPoet
```
Use beautiful Java code to generate beautiful Java code
```
JavaPoet定义了一系列类来描述java
### 样例