package org.wcong.test.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wcong on 2016/6/1.
 */
@Configuration
@MyEnableAutoConfiguration
public class CustomizeEnableAutoConfigure {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CustomizeEnableAutoConfigure.class);
        application.run(args);
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
