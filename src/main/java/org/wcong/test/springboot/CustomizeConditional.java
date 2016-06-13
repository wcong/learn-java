package org.wcong.test.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wcong on 2016/6/12.
 */
@Configuration
@EnableAutoConfiguration
public class CustomizeConditional {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CustomizeConditional.class);
        springApplication.setWebEnvironment(false);
        ConfigurableApplicationContext noneMessageConfigurableApplicationContext = springApplication.run("--logging.level.root=ERROR");
        try {
            System.out.println(noneMessageConfigurableApplicationContext.getBean(HelloWorld.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run("--message=haha", "--logging.level.root=ERROR");
        System.out.println(configurableApplicationContext.getBean(HelloWorld.class));
    }

    @Configuration
    @ConditionalOnMyProperties(name = "message")
    public static class ConditionClass {
        @Bean
        public HelloWorld helloWorld() {
            return new HelloWorld();
        }
    }

    private static class HelloWorld {
        public void pring() {
            System.out.println("hello world");
        }
    }

}
