package org.wcong.test.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wcong on 2016/5/26.
 */
@Configuration
@EnableAutoConfiguration
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Bootstrap.class);
        springApplication.run(args);
    }

    @Controller
    public static class MyController {
        @RequestMapping
        @ResponseBody
        public Map index() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("hello", "world");
            return map;
        }
    }

}
