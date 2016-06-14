package org.wcong.test.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wcong on 2016/6/14.
 */
public class JsonPropertySourceLoader implements PropertySourceLoader {
    public String[] getFileExtensions() {
        return new String[]{"json"};
    }

    public PropertySource<?> load(String name, Resource resource, String profile) throws IOException {
        if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
            Map<String, Object> result = mapPropertySource(resource);
            return new MapPropertySource(name, result);
        }
        return null;
    }

    private Map<String, Object> mapPropertySource(Resource resource) throws IOException {
        if (resource == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<Object, Object> map = objectMapper.readValue(resource.getInputStream(), Map.class);
        nestMap("", result, map);
        return result;
    }

    private void nestMap(String prefix, Map<String, Object> result, Map<Object, Object> map) {
        if (prefix.length() > 0) {
            prefix += ".";
        }
        for (Map.Entry entrySet : map.entrySet()) {
            if (entrySet.getValue() instanceof Map) {
                nestMap(prefix + entrySet.getKey(), result, (Map<Object, Object>) entrySet.getValue());
            } else {
                result.put(prefix + entrySet.getKey().toString(), entrySet.getValue());
            }
        }
    }


}
