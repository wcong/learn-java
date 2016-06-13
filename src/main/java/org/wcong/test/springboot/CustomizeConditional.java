package org.wcong.test.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wcong on 2016/6/12.
 */
@Configuration
//@EnableAutoConfiguration(excludeName = {"org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration",
//        "org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration",
//        "org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration",
//        "org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration",
//        "org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration",
//        "org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration",
//        "org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration",
//        "org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration",
//        "org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration",
//        "org.springframework.boot.autoconfigure.websocket.WebSocketMessagingAutoConfiguration"})
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
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run("--message=haha","--logging.level.root=ERROR");
        System.out.println(configurableApplicationContext.getBean(HelloWorld.class));
    }

    @Controller
    public static class MyController {
        @RequestMapping
        @ResponseBody
        public Map<String, String> index() {
            Map<String, String> map = new HashMap<String, String>();
            return map;
        }
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
