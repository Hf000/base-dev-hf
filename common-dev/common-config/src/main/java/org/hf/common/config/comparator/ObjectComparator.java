package org.hf.common.config.comparator;

import java.util.List;

/**
 * <p> 对象比对器 </p >
 * 用于对比两个对象的所有属性是否完全相等
 * @author hufei
 * @date 2022-09-06
 **/
public interface ObjectComparator {
    /**
     * 两个对象是否全相等
     *
     * @param objReq 需要比较的对象
     * @param objResp 比较参照的对象
     * @param superClass 是否比较父类字段
     * @return 两个对象是否全相等
     */
    boolean isEquals(Object objReq, Object objResp, boolean superClass);

    /**
     * 获取不相等的属性
     *
     * @param objReq 需要比较的对象
     * @param objResp 比较参照的对象
     * @param superClass 是否比较父类字段
     * @return 不相等的属性，键为属性名，值为属性类型
     */
    List<FieldInfo> getDiffFields(Object objReq, Object objResp, boolean superClass);
}