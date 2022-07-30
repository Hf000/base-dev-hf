package org.hf.common.publi.utils;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p> json工具类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 12:23
 */
@Slf4j
public class JsonUtils {

    /**
     * 将json字符串转换成Map集合,  现在可以通过fastjson替换，方法：JSON.parseObject(jsonStr,Map.class);
     * @param jsonStr json字符串
     * @return 返回map
     */
    public static Map<String, Object> jsonStrToMap(String jsonStr) {
        Map<String, Object> map = null;
        try {
            if (StringUtils.isNoneBlank(jsonStr)) {
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                if (jsonObject != null) {
                    map = new HashMap<>(jsonObject.size());
                    Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
                    for (Map.Entry<String, Object> entry : entrySet) {
                        map.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (Exception e) {
            log.error("json转换Map异常", e);
        }
        return map;
    }

    /**
     * 将json字符串转换成List<Map<K, V>>集合necessary
     * @param jsonStr json字符串
     * @return 返回list
     */
    public static List<Map<String, Object>> jsonStrToList(String jsonStr) {
        List<Map<String, Object>> rsList = null;
        try {
            if (StringUtils.isNoneBlank(jsonStr)) {
                JSONArray jsonArray = JSONArray.parseArray(jsonStr);
                if (jsonArray != null) {
                    rsList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Map<String, Object> map = new HashMap<>(jsonObject.size());
                        Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
                        for (Map.Entry<String, Object> entry : entrySet) {
                            map.put(entry.getKey(), entry.getValue());
                        }
                        rsList.add(map);
                    }
                }
            }
        } catch (Exception e) {
            log.error("json转换Map异常", e);
        }
        return rsList;
    }

}
