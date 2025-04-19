package org.hf.boot.springboot.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * <p> jackson工具类 </p >
 *
 * @author hf
 **/
public class JacksonUtil {

    private JacksonUtil(){}

    static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }

    /**
     * 将对象转换成字符串
     * fastjson和jackson区别：
     *  1.如果对象中有属性为null,fastjson默认是移除null的,转后的字符串是不包含为null的那个属性,jackson不会移除
     *  2.如果转map,fastjson会丢失属性。jackson不会丢失属性,jackson会保留为null的属性
     *  3.fastjson取出来的值不带" "双引号 ,jackson带" "双引号
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
