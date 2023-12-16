package org.hf.boot.springboot.enumerate.mvc.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hf.boot.springboot.enumerate.CommonEnum;
import org.hf.boot.springboot.enumerate.mvc.CommonEnumRegistry;
import org.hf.boot.springboot.enumerate.mvc.pojo.CommonEnumVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SpringMVC参数转换扩展 - 2: 请求体参数转换,json参数进行枚举转换 (这里以Jackson为例)
 * 自定义枚举转换及查询处理 - 5
 */
@Configuration
public class CommonEnumJsonParamConverter {
    @Autowired
    private CommonEnumRegistry enumRegistry;

    /**
     * 全局配置定制化枚举序列化/反序列化
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer commonEnumBuilderCustomizer() {
        return builder -> {
            Map<Class<?>, List<CommonEnum>> classDict = enumRegistry.getClassDict();
            classDict.forEach((aClass, commonEnums) -> {
                builder.deserializerByType(aClass, new CommonEnumJsonDeserializer(commonEnums));
                builder.serializerByType(aClass, new CommonEnumJsonSerializer());
            });

        };
    }

    static class CommonEnumJsonSerializer extends JsonSerializer<Object> {
        // 回显枚举类型时, 将CommonEnum封装为CommonEnumVO返回
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            CommonEnum commonEnum = (CommonEnum) o;
            CommonEnumVO commonEnumVO = CommonEnumVO.from(commonEnum);
            jsonGenerator.writeObject(commonEnumVO);
        }
    }

    static class CommonEnumJsonDeserializer extends JsonDeserializer<Object> {
        private final List<CommonEnum> commonEnums;

        CommonEnumJsonDeserializer(List<CommonEnum> commonEnums) {
            this.commonEnums = commonEnums;
        }

        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            // 从json中读取信息, 根据name或者code进行枚举值转换
            String value = jsonParser.readValueAs(String.class);
            return commonEnums.stream()
                    .filter(commonEnum -> commonEnum.match(value))
                    .findFirst()
                    .orElse(null);
        }
    }
}