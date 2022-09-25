package org.hf.common.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p> 通过jackson进行对象转换处理 </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
public class JacksonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    static {
        // 设置ObjectMapper相关配置
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SimpleModule model = new SimpleModule();
        model.addSerializer(Long.class, ToStringSerializer.instance);
        model.addSerializer(Long.TYPE, ToStringSerializer.instance);
        MAPPER.registerModule(model);
    }

    private JacksonUtil() {
    }

    /**
     * 将对象转换成json字符串
     *
     * @param obj 入参
     * @param <T> 声明类型
     * @return String
     */
    public static <T> String serializer(T obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            logger.error("JsonProcessingException-->{0}", var2);
            return null;
        }
    }

    /**
     * 对象转换为byte数组
     *
     * @param obj 入参
     * @return byte[]
     */
    public static byte[] serializeToBytes(Object obj) {
        try {
            return MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException var2) {
            logger.error("JsonProcessingException-->{0}", var2);
            return null;
        }
    }

    public static Map<String, Object> deserialize(String json) {
        return deserialize(json.getBytes());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, Object> deserialize(byte[] src) {
        return (Map) deserialize(src, Map.class);
    }

    public static <T> T deserialize(String json, Class<T> beanClass) {
        return StringUtils.isEmpty(json) ? null : deserialize(json.getBytes(), beanClass);
    }

    /**
     * 将byte数组转换为对象
     *
     * @param bytes     入参
     * @param beanClass 对象类型
     * @param <T>       声明类型
     * @return T
     */
    public static <T> T deserialize(byte[] bytes, Class<T> beanClass) {
        if (bytes.length != 0 && null != beanClass) {
            try {
                return MAPPER.readValue(bytes, beanClass);
            } catch (Exception var3) {
                logger.error("JsonProcessingException-->{0}", var3);
                return null;
            }
        } else {
            return null;
        }
    }
}