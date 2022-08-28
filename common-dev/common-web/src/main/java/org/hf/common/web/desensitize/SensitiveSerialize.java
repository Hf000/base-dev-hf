package org.hf.common.web.desensitize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.Objects;

import static org.hf.common.publi.constants.NumberConstant.*;
import static org.hf.common.publi.constants.CommonConstant.*;

/**
 * <p> 自定义序列化器: 对jackson序列化操作进行重写 </p >
 * @author HUFEI
 * @date 2022-08-24
 **/
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveTypeEnum type;

    private Integer prefixNoMaskLen;

    private Integer suffixNoMaskLen;

    private String maskStr;

    /**
     *  注意，如果origin为""或者null，那么至少要执行jsonGenerator.writeString(origin);保证字段有值写出去
     *  不然会报 Could not write JSON: Can not write a field name, expecting a value; nested exception is com.fasterxml.jackson.core.JsonGenerationException: Can not write a field name, expecting a value
     *  的错误
     *  序列化处理, 此方法获取createContextual方法返回的参数
     * @param origin    需要处理的字符串
     * @param jsonGenerator 这个类主要是用来生成Json格式的内容的，我们可以使用JsonFactory 的方法生成一个实例。
     * @param serializerProvider 序列化对象
     * @throws IOException exception
     */
    @Override
    public void serialize(final String origin, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isBlank(origin)){
            jsonGenerator.writeString(origin);
            return;
        }
        try{
            log.info("serialize,origin:{}",origin);
            doSerialize(origin,jsonGenerator);
        }catch(Exception e){
            log.error("SensitiveSerialize.serialize,Exception:{}", ExceptionUtils.getStackTrace(e));
            jsonGenerator.writeString(origin);
        }
    }

    /**
     * 针对不同的类型进行脱敏处理
     * @param origin 需要处理的脱敏对象
     * @param jsonGenerator json构造对象
     * @throws IOException exception
     */
    private void doSerialize(String origin, JsonGenerator jsonGenerator) throws IOException {
        int originLength = origin.length();
        int prefixLength;
        switch (type) {
            case CHINESE_NAME:
                jsonGenerator.writeString(DesensitizedUtils.desensitizedUserName(origin));
                break;
            case EMAIL:
                jsonGenerator.writeString(DesensitizedUtils.desensitizedEmail(origin));
                break;
            case MOBILE_PHONE:
                jsonGenerator.writeString(DesensitizedUtils.desensitizedPhoneNumber(origin));
                break;
            case FIXED_PHONE:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, INT_3, INT_2, ASTERISK));
                break;
            case BANK_CARD:
                jsonGenerator.writeString(DesensitizedUtils.desensitizedBankCode(origin));
                break;
            case ADDRESS:
                jsonGenerator.writeString(DesensitizedUtils.desensitizedAddress(origin));
                break;
            case ID_CARD:
                jsonGenerator.writeString(DesensitizedUtils.desensitizedIdNumber(origin));
                break;
            case OFFICER_NUMBER:
            case PASSPORT:
            case RESIDENCE_PERMIT:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, INT_0, INT_2, ASTERISK));
                break;
            case LICENCE:
            case MAC:
                if (origin.length() < INT_4) {
                    jsonGenerator.writeString(origin);
                    break;
                }
                prefixLength = origin.length() - INT_4;
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, prefixLength, INT_0, ASTERISK));
                break;
            case RESERVE_FUND:
                if (originLength != INT_9 && originLength != INT_12){
                    jsonGenerator.writeString(origin);
                    break;
                }
                prefixLength = originLength - INT_4;
                if (originLength==INT_9){
                    jsonGenerator.writeString(DesensitizedUtils.desValue(origin, prefixLength, INT_0, ASTERISK));
                    break;
                }
                prefixLength = originLength - INT_7;
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, prefixLength, INT_0, ASTERISK));
                break;
            case SOCIAL_SECURITY:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, INT_2, INT_2, ASTERISK));
                break;
            case IP:
                if (origin.length()<INT_6){
                    jsonGenerator.writeString(origin);
                    break;
                }
                prefixLength = origin.length() - INT_6;
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, prefixLength, INT_0, ASTERISK));
                break;
            case IMEI:case IDFA:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, INT_0, INT_0, ASTERISK));
                break;
            case CAR_NO:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, INT_3, INT_1, ASTERISK));
                break;
            case CAR_IDENTIFICATION_NUMBER:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, INT_4, INT_4, ASTERISK));
                break;
            case CUSTOMER:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, maskStr));
                break;
            default:
                log.info("unknow sensitive type enum:{}", type);
                jsonGenerator.writeString(origin);
        }
    }

    /**
     * 获取上下文, 此方法优先于serialize()方法执行
     * @param serializerProvider 序列化对象
     * @param beanProperty  需要序列化的对象属性
     * @return JsonSerializer json序列化对象
     * @throws JsonMappingException exception
     */
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        log.info("SensitiveSerialize.createContextual");
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
                if (sensitive == null) {
                    sensitive = beanProperty.getContextAnnotation(Sensitive.class);
                }
                if (sensitive != null) {
                    return new SensitiveSerialize(sensitive.type(), sensitive.prefixNoMaskLen(), sensitive.suffixNoMaskLen(), sensitive.maskStr());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
