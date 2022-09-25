package org.hf.common.config.comparator;

import java.util.Objects;

/**
 * <p> 字段属性实体 </p >
 * @author hufei
 * @date 2022-09-06
 **/
public class FieldInfo {
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段描述
     */
    private String fieldDesc;
    /**
     * 字段类型
     */
    private Class<?> fieldType;
    /**
     * 字段的值
     */
    private Object fieldValue;

    public FieldInfo() {
    }

    public FieldInfo(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public FieldInfo(String fieldName, Class<?> fieldType, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldValue = fieldValue;
    }

    public FieldInfo(String fieldName, String fieldDesc, Class<?> fieldType, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
        this.fieldType = fieldType;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof FieldInfo) {
            FieldInfo fieldInfo = (FieldInfo)obj;
            return Objects.equals(fieldName, fieldInfo.getFieldName()) && Objects.equals(fieldType, fieldInfo.getFieldType()) && Objects.equals(fieldValue, fieldInfo.getFieldValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, fieldDesc, fieldType, fieldValue);
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldDesc='" + fieldDesc + '\'' +
                ", fieldType=" + fieldType +
                ", fieldValue=" + fieldValue +
                '}';
    }

}