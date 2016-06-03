package org.wcong.test.springboot;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * MyEnableAutoConfigurationImport
 * Created by wcong on 2016/6/3.
 */
public class MyEnableAutoConfigurationImport implements DeferredImportSelector, BeanClassLoaderAware {
    private ClassLoader classLoader;

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> beanNames = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, classLoader);
        return beanNames.toArray(new String[beanNames.size()]);
    }
}
