package org.hf.boot.springboot.dynamic.statement;

import com.alibaba.fastjson2.JSONObject;

public interface DynamicStatementDataApiService {

    Object postJson(String path, JSONObject paramJson);
}
