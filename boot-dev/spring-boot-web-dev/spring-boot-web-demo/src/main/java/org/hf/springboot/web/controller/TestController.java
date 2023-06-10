package org.hf.springboot.web.controller;

import com.alibaba.fastjson2.JSON;
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
     * 请求入参示例
     * {
     *     "methodName":"getCurrentLdspClockActInfo",
     *     "serviceName":"oprActivityInfoServiceImpl",
     *     "paraJsonStr":"{\"activityCode\":\"\",\"activityType\":\"CLOCK_PASS\"}"
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
        /*if (StringUtils.isEmpty(paraJsonStr) && paraList == null && StringUtils.isEmpty(paraJsonArrayStr)) {
            throw new CustomWebException("paraJsonStr paraList paraJsonArrayStr 不能同时为空");
        }*/
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
                    resultObj = method.invoke(object, paraList);
                }
                break;
            }
        }
        return ResponseUtil.success(resultObj);
    }
}