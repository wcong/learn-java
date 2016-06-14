package org.wcong.test.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wcong on 2016/6/14.
 */
@Configuration
@EnableAutoConfiguration
public class CustomizePropertySourceLoader {

    @Value("${customize.property.message}")
    private String message;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CustomizePropertySourceLoader.class);
        springApplication.setWebEnvironment(false);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        CustomizePropertySourceLoader customizePropertySourceLoader = configurableApplicationContext.getBean(CustomizePropertySourceLoader.class);
        System.out.println(customizePropertySourceLoader.message);
    }

}
