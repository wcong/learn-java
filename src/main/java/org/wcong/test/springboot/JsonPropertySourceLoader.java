package org.wcong.test.springboot;

import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wcong on 2016/6/14.
 */
public class JsonPropertySourceLoader implements PropertySourceLoader {
    public String[] getFileExtensions() {
        return new String[]{"json"};
    }

    public PropertySource<?> load(String name, Resource resource, String profile) throws IOException {
        Map<String, Object> result = mapPropertySource(resource);
        return new MapPropertySource(name, result);
    }

    private Map<String, Object> mapPropertySource(Resource resource) throws IOException {
        if (resource == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = parser.parseMap(readFile(resource));
        nestMap("", result, map);
        return result;
    }

    private String readFile(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();
        List<Byte> byteList = new LinkedList<Byte>();
        byte[] readByte = new byte[1024];
        int length;
        while ((length = inputStream.read(readByte)) > 0) {
            for (int i = 0; i < length; i++) {
                byteList.add(readByte[i]);
            }
        }
        byte[] allBytes = new byte[byteList.size()];
        int index = 0;
        for (Byte soloByte : byteList) {
            allBytes[index] = soloByte;
            index += 1;
        }
        return new String(allBytes);
    }

    private void nestMap(String prefix, Map<String, Object> result, Map<String, Object> map) {
        if (prefix.length() > 0) {
            prefix += ".";
        }
        for (Map.Entry entrySet : map.entrySet()) {
            if (entrySet.getValue() instanceof Map) {
                nestMap(prefix + entrySet.getKey(), result, (Map<String, Object>) entrySet.getValue());
            } else {
                result.put(prefix + entrySet.getKey().toString(), entrySet.getValue());
            }
        }
    }


}
