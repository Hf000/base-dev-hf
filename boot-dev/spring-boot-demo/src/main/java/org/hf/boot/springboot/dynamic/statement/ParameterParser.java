package org.hf.boot.springboot.dynamic.statement;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 参数解析工具
 */
@Component
public class ParameterParser {

    public Map<String,Object> parseMap(JSONObject paramJson, List<ParameterDTO> paramConfigList){
        Map<String,Object> paramMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(paramConfigList)){
            for (ParameterDTO parameterDTO : paramConfigList) {
                // 1.获取json数值
                String name = parameterDTO.getName();
                Object value = null;
                DataFieldTypeEnum fieldTypeEnum = DataFieldTypeEnum.matchByCode(parameterDTO.getFieldType());
                if (fieldTypeEnum == null) {
                    value = paramJson.getString(name);
                } else {
                    switch (fieldTypeEnum) {
                        case NUMBER:
                            value = paramJson.getBigDecimal(name);
                            break;
                        case DATE:
                            value = paramJson.getDate(name);
                            break;
                        case BOOLEAN:
                            value = paramJson.getBoolean(name);
                            break;
                        case ARRAY:
                            if(StringUtils.isNotBlank(parameterDTO.getArrayFieldType())){
                                // 根据类型获取转换器
                                TypeReference<?> typeReference = getArrayTypeReference(parameterDTO.getArrayFieldType());
                                // 将参数转换成指定的类型
                                value = paramJson.getObject(name, typeReference);
                            }
                            break;
                        default:
                            value = paramJson.getString(name);
                    }
                }
                if(BooleanUtils.isTrue(parameterDTO.getRequired())){
                    if(Objects.isNull(value)){
                        throw new BusinessException(name+"不能为空");
                    }
                }
                paramMap.put(name, value);
            }
            return paramMap;
        }
        return null;
    }


    private TypeReference<?> getArrayTypeReference(String arrayFieldType){
        TypeReference<?> typeReference;
        DataFieldTypeEnum fieldTypeEnum = DataFieldTypeEnum.matchByCode(arrayFieldType);
        if (fieldTypeEnum == null) {
            typeReference = new TypeReference<List<String>>(){};
        } else {
            switch (fieldTypeEnum) {
                case NUMBER:
                    typeReference = new TypeReference<List<BigDecimal>>(){};
                    break;
                case DATE:
                    typeReference = new TypeReference<List<Date>>(){};
                    break;
                case STRING:
                    typeReference = new TypeReference<List<String>>(){};
                    break;
                default:
                    typeReference = new TypeReference<List<Boolean>>(){};
            }
        }
        return typeReference;
    }
}