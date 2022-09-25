package org.hf.common.config.comparator;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p> 对象对比器抽象类 </p >
 *
 * @author hufei
 * @date 2022-09-06
 **/
public abstract class AbstractObjectComparator implements ObjectComparator {

    /**
     * 属性包装类型集合
     */
    private static final List<Class<?>> PROPERTY_TYPE = Arrays.asList(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Character.class, Boolean.class, String.class);
    /**
     * 包含的字段集合
     */
    private List<String> includeFields;
    /**
     * 排除的字段集合
     */
    private List<String> excludeFields;
    /**
     * 如果对象类型不一致,true:比对两个类都包含的字段,false:比对两个类字段的并集
     * 默认为 true
     */
    private boolean bothField = true;

    public AbstractObjectComparator() {
        includeFields = Collections.emptyList();
        excludeFields = Collections.emptyList();
    }

    public AbstractObjectComparator(boolean bothField) {
        includeFields = Collections.emptyList();
        excludeFields = Collections.emptyList();
        this.bothField = bothField;
    }

    public AbstractObjectComparator(List<String> includeFields, List<String> excludeFields) {
        this.includeFields = includeFields;
        this.excludeFields = excludeFields;
    }

    public AbstractObjectComparator(List<String> includeFields, List<String> excludeFields, boolean bothField) {
        this.includeFields = includeFields;
        this.excludeFields = excludeFields;
        this.bothField = bothField;
    }

    public List<String> getIncludeFields() {
        return includeFields;
    }

    public void setIncludeFields(List<String> includeFields) {
        this.includeFields = includeFields;
    }

    public List<String> getExcludeFields() {
        return excludeFields;
    }

    public void setExcludeFields(List<String> excludeFields) {
        this.excludeFields = excludeFields;
    }

    public boolean isBothField() {
        return bothField;
    }

    public void setBothField(boolean bothField) {
        this.bothField = bothField;
    }

    /**
     * 只要没有不相等的属性，两个对象就全相等
     *
     * @param objReq  需要比较的对象
     * @param objResp 比较参照的对象
     * @param superClass 是否比较父类字段
     * @return 两个对象是否全相等
     */
    @Override
    public boolean isEquals(Object objReq, Object objResp, boolean superClass) {
        List<FieldInfo> diffFields = getDiffFields(objReq, objResp, superClass);
        return CollectionUtil.isEmpty(diffFields);
    }

    /**
     * 对比两个对象的指定字段信息是否相等
     *
     * @param fieldReq  需要比较的字段对象
     * @param fieldResp 比较参照的字段对象
     * @return 属性是否相等
     */
    protected boolean isFieldEquals(FieldInfo fieldReq, FieldInfo fieldResp) {
        // 如果是排除了的字段则不比较, 如果指定了包含字段且不在包含字段内也不比较
        if (isExclude(fieldReq) || isExclude(fieldResp) || !isInclude(fieldReq) || !isInclude(fieldResp)) {
            return true;
        }
        return isObjectEquals(fieldReq.getFieldValue(), fieldResp.getFieldValue());
    }

    /**
     * 比较对象是否相等
     *
     * @param objReq  需要比较的对象
     * @param objResp 比较参照的对象
     * @return boolean
     */
    protected boolean isObjectEquals(Object objReq, Object objResp) {
        if (objReq == null && objResp == null) {
            return true;
        } else if (objReq == null || objResp == null) {
            return false;
        }
        // 判断是否是基本类型
        if (isFieldBaseType(objReq, objResp)) {
            return CollectionUtil.isEmpty(compareFieldBaseType(objReq, objResp));
        }
        if (objReq instanceof Collection && objResp instanceof Collection) {
            // 如果是集合类型
            return Objects.deepEquals(((Collection<?>) objReq).toArray(), ((Collection<?>) objResp).toArray());
        }
        return Objects.deepEquals(objReq, objResp);
    }

    public static void main(String[] args) {
        System.out.println(compareFieldBaseType(null, 123));
    }

    /**
     * 如果是基本数据类型或是其包装类型,包含String,的对象则直接进行比对
     *
     * @param objReq  需要比较的对象
     * @param objResp 比较参照的对象
     * @return 相等则返回空集合, 不相等则返回对应的集合对象, 集合元素为objReq的取值
     */
    protected static List<FieldInfo> compareFieldBaseType(Object objReq, Object objResp) {
        if (Objects.equals(objReq, objResp)) {
            return Collections.emptyList();
        } else {
            Object obj = objReq == null ? objResp : objReq;
            Class<?> clazz = obj.getClass();
            // 不等的字段名称使用类的名称
            return Collections.singletonList(new FieldInfo(clazz.getSimpleName(), clazz, objReq));
        }
    }

    /**
     * 判断是否为基本数据类型或其包装类型,包含String
     *
     * @param objReq  需要比较的对象
     * @param objResp 比较参照的对象
     * @return boolean
     */
    protected static boolean isFieldBaseType(Object objReq, Object objResp) {
        Object obj = objReq == null ? objResp : objReq;
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() || PROPERTY_TYPE.contains(clazz);
    }

    /**
     * 判断是否包含了此字段
     *
     * @param fieldInfo 字段信息
     * @return ture:已包含,false:未包含
     */
    protected boolean isInclude(FieldInfo fieldInfo) {
        // 如果没有指定则默认为全部包含, 如果为null默认为包含
        if (CollectionUtil.isEmpty(includeFields) || fieldInfo == null) {
            return true;
        }
        return includeFields.contains(fieldInfo.getFieldName());
    }

    /**
     * 判断是否排除了此字段
     *
     * @param fieldInfo 字段信息
     * @return ture:已排除,false:未排除
     */
    protected boolean isExclude(FieldInfo fieldInfo) {
        // 如果没有指定则默认为全部未排除
        if (CollectionUtil.isEmpty(excludeFields)) {
            return false;
        }
        // null是未排除
        return fieldInfo != null && excludeFields.contains(fieldInfo.getFieldName());
    }

    /**
     * 根据配置的规则决定取两个对象字段的交集或并集
     *
     * @param fieldReq  需要比较的对象字段集合
     * @param fieldResp 比较参照的对象字段集合
     * @return List<String>
     */
    protected List<String> getAllFieldNames(List<String> fieldReq, List<String> fieldResp) {
        if (CollectionUtil.isEmpty(fieldReq) && CollectionUtil.isEmpty(fieldResp)) {
            return new ArrayList<>();
        }
        if (isBothField()) {
            // 取交集
            if (CollectionUtil.isEmpty(fieldReq) || CollectionUtil.isEmpty(fieldResp)) {
                return new ArrayList<>();
            } else {
                return fieldReq.stream().filter(fieldResp::contains).collect(Collectors.toList());
            }
        } else {
            // 取并集
            if (CollectionUtil.isEmpty(fieldReq)) {
                return fieldResp;
            } else if (CollectionUtil.isEmpty(fieldResp)) {
                return fieldReq;
            } else {
                return new ArrayList<>(CollectionUtil.unionDistinct(fieldReq, fieldResp));
            }
        }
    }

}