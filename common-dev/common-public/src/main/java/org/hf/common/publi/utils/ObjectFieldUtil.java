package org.hf.common.publi.utils;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.annotation.FieldDesc;
import org.hf.common.publi.constants.DateFormatConstant;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * <p> 对象属性处理工具类 </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
@Slf4j
public class ObjectFieldUtil {

    /**
     * 获取对象的属性描述,属性名称,属性值拼接字符串
     * @param obj 入参
     * @return String
     */
    public static String getObjectFieldsValue(Object obj) {
        if (obj == null) {
            return null;
        }
        // 获取字段的值及字段描述
        try {
            StringBuilder content = new StringBuilder();
            for (Field field : obj.getClass().getDeclaredFields()) {
                FieldInfo fieldInfo = getFieldInfo(obj, field);
                if (fieldInfo == null) {
                    break;
                }
                String fieldName = fieldInfo.getFieldName();
                String fieldValue = fieldInfo.getFieldValue();
                String fieldDesc = fieldInfo.getFieldDesc();
                if (StringUtils.isNotBlank(fieldValue) && StringUtils.isNotBlank(fieldDesc) && StringUtils.isNotBlank(fieldName)) {
                    content.append(fieldName).append(fieldDesc).append(fieldValue).append(";");
                }
            }
            return content.toString();
        } catch (Exception e) {
            log.error("获取字段描述及值失败", e);
        }
        return null;
    }

    /**
     * 比较两个对象的属性的值是否相等,返回不相等属性的描述字符串拼接
     * @param objReq 入参
     * @param objResp 入参
     * @return String
     */
    public static String getObjectDiffFieldsValue(Object objReq, Object objResp) {
        if (objReq == null || objResp == null || objReq == objResp || objReq.equals(objResp) || objReq.getClass() != objResp.getClass()) {
            return null;
        }
        // 获取字段的值及字段描述
        try {
            StringBuilder content = new StringBuilder();
            for (Field field : objReq.getClass().getDeclaredFields()) {
                FieldInfo fieldReq = getFieldInfo(objReq, field);
                FieldInfo fieldResp = getFieldInfo(objResp, field);
                if (fieldReq == null) {
                    break;
                }
                String fieldValueReq = fieldReq.getFieldValue();
                String fieldValueResp = fieldResp.getFieldValue();
                String fieldDesc = fieldReq.getFieldDesc();
                String fieldName= fieldReq.getFieldName();
                if (StringUtils.isNotBlank(fieldValueReq) && !fieldValueReq.equals(fieldValueResp)) {
                    content.append(fieldDesc).append(";");
                }
            }
            return content.toString();
        } catch (Exception e) {
            log.error("同类型对象字段值对比失败", e);
        }
        return null;
    }

    /**
     * 获取对象的字段信息
     * @param object 入参
     * @param field 入参
     * @return FieldInfo
     */
    private static FieldInfo getFieldInfo(Object object, Field field) {
        if (object == null || field == null) {
            return null;
        }
        try {
            field.setAccessible(true);
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setFieldName(field.getName());
            Object fieldValue = field.get(object);
            // 对指定的数据类型进行转换
            if (fieldValue instanceof Boolean) {
                fieldInfo.setFieldValue((booleanEscape((Boolean)fieldValue)));
            } else if (fieldValue instanceof Date) {
                fieldInfo.setFieldValue(DateUtil.format((Date) fieldValue, DateFormatConstant.YYYY_MM_DD_HH_MM_SS));
            } else {
                fieldInfo.setFieldValue(fieldValue == null ? null : String.valueOf(fieldValue));
            }
            FieldDesc annotation = field.getAnnotation(FieldDesc.class);
            if (annotation != null) {
                fieldInfo.setFieldDesc(annotation.value());
            }
            return fieldInfo;
        } catch (Exception e) {
            log.error("获取对象字段信息失败!", e);
        }
        return null;
    }

    public static String booleanEscape(Boolean bool) {
        if (Boolean.TRUE.equals(bool)) {
            return "是";
        } else {
            return Boolean.FALSE.equals(bool) ? "否" : "无";
        }
    }

}

/**
 * 字段信息实体
 */
@Setter
@Getter
@ToString
class FieldInfo {

    private String fieldName;
    private String fieldValue;
    private String fieldDesc;

}
