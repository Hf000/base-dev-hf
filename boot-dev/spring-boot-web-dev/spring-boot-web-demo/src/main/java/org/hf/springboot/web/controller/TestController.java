package org.hf.springboot.web.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.utils.SpringBeanUtil;
import org.hf.common.web.pojo.vo.ResponseVO;
import org.hf.common.web.utils.ResponseUtil;
import org.hf.springboot.web.exception.CustomWebException;
import org.hf.springboot.web.pojo.bo.TestReqBO;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * <p> 测试 </p >
 * @author hufei
 * @date 2023-04-11
 **/
@Slf4j
@RestController
@RequestMapping(value = "/test")
public class TestController {

    /**
     * 1.json参数请求入参示例
     * {
     *     "methodName":"getCurrentLdspClockActInfo",
     *     "serviceName":"oprActivityInfoServiceImpl",
     *     "paraJsonStr":"{\"activityCode\":\"\",\"activityType\":\"CLOCK_PASS\"}"
     * }
     * 2.数组参数请求示例
     * {
     *     "methodName":"方法名称",
     *     "serviceName":"服务名称",
     *     "paraList":[对象1, 对象2] （如果不是基本数据类型的包装类，对象最好用json方式传递）
     * }
     * @param req 入参
     * @return 执行结果
     * @throws Exception 异常
     */
    @PostMapping(value = "/testServiceMethod")
    public ResponseVO<Object> testServiceMethod(@RequestBody @Validated TestReqBO req) throws Exception {
        String methodName = req.getMethodName();
        String serviceName = req.getServiceName();
        String paraJsonStr = req.getParaJsonStr();
        Object[] paraList = req.getParaList();
        String paraJsonArrayStr = req.getParaJsonArrayStr();
        Object object = SpringBeanUtil.getBean(serviceName);
        if (object == null) {
            throw new CustomWebException("服务不存在");
        }
        Method[] methods = object.getClass().getMethods();
        Object resultObj = null;
        for (Method method : methods) {
            ReflectionUtils.makeAccessible(method);
            if (methodName.equals(method.getName())) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (StringUtils.isEmpty(paraJsonStr) && StringUtils.isEmpty(paraJsonArrayStr) && Objects.isNull(paraList)) {
                    resultObj = method.invoke(object);
                } else if (StringUtils.isNotEmpty(paraJsonStr)) {
                    resultObj = method.invoke(object, JSON.parseObject(paraJsonStr, parameterTypes[0]));
                } else if (StringUtils.isNotEmpty(paraJsonArrayStr)) {
                    resultObj = method.invoke(object, JSON.parseArray(paraJsonArrayStr, Object.class));
                } else if (!Objects.isNull(paraList)) {
                    // 兼容对象数据的json格式
                    for (int i = 0; i < paraList.length; i++) {
                        if (paraList[i] instanceof Map) {
                            paraList[i] = JSONObject.parseObject(JSONObject.toJSONString(paraList[i]), parameterTypes[i]);
                        }
                    }
                    resultObj = method.invoke(object, paraList);
                }
                break;
            }
        }
        return ResponseUtil.success(resultObj);
    }
}