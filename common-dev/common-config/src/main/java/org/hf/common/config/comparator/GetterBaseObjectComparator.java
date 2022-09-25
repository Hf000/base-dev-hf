package org.hf.common.config.comparator;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 基于类getter方法实现的对象比对器 </p >
 *
 * @author hufei
 * @date 2022-09-07
 **/
@Slf4j
public class GetterBaseObjectComparator extends AbstractObjectComparator {

    private static final String GET = "get";
    private static final String IS = "is";
    private static final String GET_IS = "get|is";
    private static final String GET_CLASS = "getClass";
    /**
     * 缓存类的所有字段方法信息
     */
    private static final Map<Class<?>, Map<String, Method>> CACHE = new ConcurrentHashMap<>();

    public GetterBaseObjectComparator() {
    }

    public GetterBaseObjectComparator(boolean bothField) {
        super(bothField);
    }

    public GetterBaseObjectComparator(List<String> includeFields, List<String> excludeFields) {
        super(includeFields, excludeFields);
    }

    public GetterBaseObjectComparator(List<String> includeFields, List<String> excludeFields, boolean bothField) {
        super(includeFields, excludeFields, bothField);
    }

    /**
     * 获取不同的字段集合, 此方式暂时没set字段描述信息
     *
     * @param objReq  需要比较的对象
     * @param objResp 比较参照的对象
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
            // 获取所有字段方法
            List<String> objReqFieldNames = new ArrayList<>();
            List<String> objRespFieldNames = new ArrayList<>();
            Map<String, Method> objReqGetters = getAllGetters(objReq, superClass, objReqFieldNames);
            Map<String, Method> objRespGetters = getAllGetters(objResp, superClass, objRespFieldNames);
            allFieldNames = getAllFieldNames(objReqFieldNames, objRespFieldNames);
            List<FieldInfo> diffFields = new ArrayList<>();
            for (String fieldName : allFieldNames) {
                Method objReqGetterMethod = objReqGetters.get(fieldName);
                Method objRespGetterMethod = objRespGetters.get(fieldName);
                FieldInfo objReqFieldInfo = null;
                FieldInfo objRespFieldInfo = null;
                if (objReqGetterMethod != null) {
                    Object objReqFieldValue = objReqGetterMethod.invoke(objReq);
                    Class<?> objReqFieldType = getReturnType(objReqGetterMethod);
                    objReqFieldInfo = new FieldInfo(fieldName, objReqFieldType, objReqFieldValue);
                }
                if (objRespGetterMethod != null) {
                    Object objRespFieldValue = objRespGetterMethod.invoke(objResp);
                    Class<?> objRespFieldType = getReturnType(objRespGetterMethod);
                    objRespFieldInfo = new FieldInfo(fieldName, objRespFieldType, objRespFieldValue);
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
     * 获取返回类型
     *
     * @param method 方法对象
     * @return Class<?>
     */
    private Class<?> getReturnType(Method method) {
        return method == null ? null : method.getReturnType();
    }

    /**
     * 获取类中的所有 getter 方法
     *
     * @param obj        对象
     * @param superClass 是否获取父类字段方法
     * @param fieldNames 字段集合
     * @return Map<String, Method>
     */
    private Map<String, Method> getAllGetters(Object obj, boolean superClass, List<String> fieldNames) {
        if (obj == null) {
            return Collections.emptyMap();
        }
        return CACHE.computeIfAbsent(obj.getClass(), key -> {
            Map<String, Method> getters = new HashMap<>();
            Class<?> clazz = key;
            while (clazz != Object.class) {
                // 获取对象方法
                Method[] methods = clazz.getDeclaredMethods();
                for (Method m : methods) {
                    // getter 方法必须是 public 且没有参数的
                    if (!Modifier.isPublic(m.getModifiers()) || m.getParameterTypes().length > 0) {
                        continue;
                    }
                    if (m.getReturnType() == Boolean.class || m.getReturnType() == boolean.class) {
                        // 如果返回值是 boolean 则兼容 isXxx 的写法
                        if (m.getName().startsWith(IS)) {
                            String fieldName = firstCharacterLowercase(m.getName().replaceFirst(IS, ""));
                            getters.put(fieldName, m);
                            fieldNames.add(fieldName);
                            continue;
                        }
                    }
                    // 以get开头但排除getClass()方法
                    if (m.getName().startsWith(GET) && !GET_CLASS.equals(m.getName())) {
                        String fieldName = firstCharacterLowercase(m.getName().replaceFirst(GET_IS, ""));
                        getters.put(fieldName, m);
                        fieldNames.add(fieldName);
                    }
                }
                if (superClass) {
                    // 获取父类信息, 以便获取父类字段
                    clazz = clazz.getSuperclass();
                }
            }
            return getters;
        });
    }

    /**
     * 使首字母小写
     *
     * @param str 入参
     * @return String
     */
    private static String firstCharacterLowercase(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        // 获取指定索引处的字符代码值
        final int firstCodepoint = str.codePointAt(0);
        // 将大写字母转换成小写
        final int newCodePoint = Character.toLowerCase(firstCodepoint);
        // 如果首字母小写则直接返回
        if (firstCodepoint == newCodePoint) {
            return str;
        }
        // 定义字符串个字符代码数组
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        // 将转换后的首个字符赋值
        newCodePoints[outOffset++] = newCodePoint;
        // 赋值除首个字符之后的字符
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint;
            inOffset += Character.charCount(codepoint);
        }
        // 返回字符串
        return new String(newCodePoints, 0, outOffset);
    }

}