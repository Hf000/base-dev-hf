package org.hf.common.publi.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> list集合工具类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 12:23
 */
public class TypeConvertUtils {

    /**
     * 将Object对象转换成存储对应类型的List
     * @param obj   需要转换的对象
     * @param clazz 需要转换的类型
     * @param <T>   数据类型
     * @return 返回集合
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        try {
            if (obj != null) {
                List<T> result = new ArrayList<T>();
                if(obj instanceof List<?>) {
                    for (Object o : (List<?>) obj) {
                        result.add(clazz.cast(o));
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Object转换成指定类型
     * @param object 需要转换的对象
     * @param clazz 指定类型
     * @param <T> 返回类型声明
     * @return 返回声明类型对象
     */
    public static <T> T castObject(Object object, Class<T> clazz) {
        // 判断该对象能不能转换成这个类型
        if (clazz.isInstance(object)) {
            // 将该对象转换成指定的类型
            return clazz.cast(object);
        }
        throw new RuntimeException("can not cast " + object.getClass().getName() + " to " + clazz.getName());
    }

}
