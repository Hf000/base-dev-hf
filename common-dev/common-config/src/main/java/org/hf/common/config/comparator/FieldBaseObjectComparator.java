package org.hf.common.config.comparator;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.publi.annotation.FieldDesc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 基于类字段实现的对象比对器 </p >
 *
 * @author hufei
 * @date 2022-09-07
 **/
@Slf4j
public class FieldBaseObjectComparator extends AbstractObjectComparator {

    private static final String ALL_FIELD_NAME = "allFieldName";
    /**
     * 缓存类的所有字段信息
     */
    private static final Map<Class<?>, Map<String, Field>> CLASS_INFO_MAP = new ConcurrentHashMap<>();

    public FieldBaseObjectComparator() {
    }

    public FieldBaseObjectComparator(boolean bothField) {
        super(bothField);
    }

    public FieldBaseObjectComparator(List<String> includeFields, List<String> excludeFields, boolean bothField) {
        super(includeFields, excludeFields, bothField);
    }

    public FieldBaseObjectComparator(List<String> includeFields, List<String> excludeFields) {
        super(includeFields, excludeFields);
    }

    /**
     * 获取不同的字段集合
     *
     * @param objReq  需要比较的对象
     * @param objResp 比较参照的对象
     * @param superClass 是否比较父类中的字段
     * @return List<FieldInfo> 返回objReq对象中和objResp对象不一样的字段信息
     */
    @Override
    public List<FieldInfo> getDiffFields(Object objReq, Object objResp, boolean superClass) {
        try {
            if (objReq == objResp) {
                return Collections.emptyList();
            }
            // 判断是否为简单数据类型
            if (isFieldBaseType(objReq, objResp)) {
                return compareFieldBaseType(objReq, objResp);
            }
            List<String> allFieldNames;
            // 获取所有字段
            List<String> objReqFieldNames = new ArrayList<>();
            List<String> objRespFieldNames = new ArrayList<>();
            Map<String, Field> objReqFields = getAllFields(objReq, superClass, objReqFieldNames);
            Map<String, Field> objRespFields = getAllFields(objResp, superClass, objRespFieldNames);
            allFieldNames = getAllFieldNames(objReqFieldNames, objRespFieldNames);
            List<FieldInfo> diffFields = new ArrayList<>();
            for (String fieldName : allFieldNames) {
                Field objReqField = objReqFields.get(fieldName);
                Field objRespField = objRespFields.get(fieldName);
                FieldInfo objReqFieldInfo = null;
                FieldInfo objRespFieldInfo = null;
                if (objReqField != null) {
                    objReqFieldInfo = getFieldInfo(objReq, objReqField);
                }
                if (objRespField != null) {
                    objRespFieldInfo = getFieldInfo(objResp, objRespField);
                }
                if (!isFieldEquals(objReqFieldInfo, objRespFieldInfo)) {
                    diffFields.add(objReqFieldInfo);
                }
            }
            return diffFields;
        } catch (Exception e) {
            log.error("Object Field Compare Error", e);
        }
        return null;
    }

    /**
     * 获取对象的所有字段对象
     *
     * @param obj        对象
     * @param superClass 是否获取父类字段信息
     * @param fieldNames 对象字段名称
     * @return Map<String, Field>
     */
    private Map<String, Field> getAllFields(Object obj, boolean superClass, List<String> fieldNames) {
        if (obj == null) {
            return Collections.emptyMap();
        }
        // 判断key是否存在,存在则执行操作,不存在则先放入key再执行操作
        return CLASS_INFO_MAP.computeIfAbsent(obj.getClass(), key -> {
            Map<String, Field> fieldMap = new HashMap<>();
            Class<?> clazz = key;
            while (clazz != Object.class) {
                // 获取所有的字段信息
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    // 判断类字段是否为合成类字段(通过字节码注入方式添加的字段)
                    if (!field.isSynthetic()) {
                        fieldMap.put(field.getName(), field);
                        fieldNames.add(field.getName());
                    }
                }
                if (superClass) {
                    // 获取父类信息, 以便获取父类字段
                    clazz = clazz.getSuperclass();
                }
            }
            return fieldMap;
        });
    }

    /**
     * 获取对象指定的字段信息
     *
     * @param object 对象
     * @param field  对象字段信息
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
            fieldInfo.setFieldValue(fieldValue);
            FieldDesc annotation = field.getAnnotation(FieldDesc.class);
            if (annotation != null) {
                fieldInfo.setFieldDesc(annotation.value());
            }
            fieldInfo.setFieldType(field.getType());
            return fieldInfo;
        } catch (Exception e) {
            log.error("获取对象字段信息失败!", e);
        }
        return null;
    }

}