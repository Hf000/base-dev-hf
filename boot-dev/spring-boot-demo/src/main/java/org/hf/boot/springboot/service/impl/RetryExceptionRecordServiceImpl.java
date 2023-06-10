package org.hf.boot.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.dao.RetryExceptionRecordMapper;
import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.pojo.dto.InvokeMethodReq;
import org.hf.boot.springboot.pojo.dto.RetryExceptionReq;
import org.hf.boot.springboot.pojo.dto.RetryExceptionRequestInfoDTO;
import org.hf.boot.springboot.pojo.entity.RetryExceptionRecord;
import org.hf.boot.springboot.service.RetryExceptionRecordService;
import org.hf.boot.springboot.utils.HttpClientUtils;
import org.hf.boot.springboot.utils.RestTemplateUtil;
import org.hf.boot.springboot.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p> 重试异常接口实现 </p >
 * 自定义重试异常实现 - 4
 * @author HUFEI
 * @date 2023-06-07
 **/
@Slf4j
@Service
public class RetryExceptionRecordServiceImpl implements RetryExceptionRecordService {

    @Autowired
    private RetryExceptionRecordMapper retryExceptionRecordMapper;

    @Override
    public void saveExceptionRecord(RetryExceptionRecord exceptionEntity) {
        if (exceptionEntity == null) {
            return;
        }
        retryExceptionRecordMapper.insertSelective(exceptionEntity);
    }

    @Override
    public void retryExceptionRecord(RetryExceptionReq req) {
        log.info("retryRecordException req:{}", req);
        RetryExceptionRecord exceptionRecord = retryExceptionRecordMapper.selectByPrimaryKey(req.getId());
        if (exceptionRecord == null) {
            throw new BusinessException("暂无该异常记录");
        }
        Object result = null;
        RetryExceptionRequestInfoDTO requestInfoDTO = JSONObject.parseObject(exceptionRecord.getRequestInfo(), RetryExceptionRequestInfoDTO.class);
        String retryType = req.getRetryType() == null ? exceptionRecord.getRetryType() : req.getRetryType().getCode();
        if ("METHOD".equalsIgnoreCase(retryType)) {
            InvokeMethodReq methodReq = new InvokeMethodReq();
            methodReq.setServiceName(requestInfoDTO.getServiceName());
            methodReq.setMethodName(requestInfoDTO.getMethodName());
            Map<String, Object> methodParams = requestInfoDTO.getMethodParams();
            List<Object> paramValues = new ArrayList<>();
            methodParams.forEach((k, v) -> paramValues.add(v));
            if (methodParams.size() == 1) {
                Object obj = paramValues.get(0);
                if (obj instanceof Map) {
                    methodReq.setParaJsonStr(JSONObject.toJSONString(obj));
                } else if (obj instanceof List) {
                    methodReq.setParaJsonArrayStr(JSONObject.toJSONString(obj));
                }
            } else if (methodParams.size() > 1) {
                methodReq.setParaList(paramValues.toArray());
            }
            log.info("invokeServiceMethod req {}", methodReq);
            result = invokeServiceMethod(methodReq);
        } else if ("URL".equalsIgnoreCase(retryType)) {
            HttpHeaders headers = new HttpHeaders();
            requestInfoDTO.getRequestHeaders().forEach((k,v) -> headers.set(k, String.valueOf(v)));
            if (CollectionUtils.isNotEmpty(req.getCookies())) {
                headers.put(HttpHeaders.COOKIE, req.getCookies().stream().map(String::valueOf).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(req.getHeaders())) {
                req.getHeaders().forEach(headerInfo -> headers.set(headerInfo.getKey(), String.valueOf(headerInfo.getValue())));
            }
            String requestUrl = requestInfoDTO.getRequestUrl();
            String requestPathParams = requestInfoDTO.getRequestPathParams();
            String requestBodyParams = requestInfoDTO.getRequestBodyParams();
            if (StringUtils.isNotBlank(requestPathParams)) {
                requestUrl += "?" + requestPathParams;
            }
            HttpMethod method;
            if ("POST".equalsIgnoreCase(requestInfoDTO.getRequestType())) {
                method = HttpMethod.POST;
            } else if ("GET".equalsIgnoreCase(requestInfoDTO.getRequestType())) {
                method = HttpMethod.GET;
            } else {
                log.error("retryExceptionRecord 请求方式异常{}", requestInfoDTO.getRequestType());
                return;
            }
            log.info("doHttpExchange requestUrl={} headers={} requestBodyParams={}", requestUrl, headers, requestBodyParams);
            result = RestTemplateUtil.doHttpExchange(requestUrl, method, headers, requestBodyParams);
        }
        log.info("retryExceptionRecord result {}", result);
    }

    private String doHttpsPost(String url, Map<String, String> headerMap, String bodyJsonString) {
        String httpRes = HttpClientUtils.postJsonSetHeaders(url, bodyJsonString, headerMap,"UTF-8");
        log.info("do post https === url:{},httpRes:{}", url, httpRes);
        return httpRes;
    }

    @Override
    public Object invokeServiceMethod(InvokeMethodReq req) {
        String methodName = req.getMethodName();
        String serviceName = req.getServiceName();
        String paraJsonStr = req.getParaJsonStr();
        Object[] paraList = req.getParaList();
        String paraJsonArrayStr = req.getParaJsonArrayStr();
        if (StringUtils.isBlank(methodName) || StringUtils.isBlank(serviceName)) {
            throw new BusinessException("服务名称或者方法名称为空");
        }
        serviceName = StrUtil.lowerFirst(serviceName);
        Object object = SpringContextUtil.getBean(serviceName);
        Method[] methods = object.getClass().getMethods();
        Object resultObj = null;
        for (Method method : methods) {
            ReflectionUtils.makeAccessible(method);
            if (methodName.equals(method.getName())) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                try {
                    if (StringUtils.isEmpty(paraJsonStr) && StringUtils.isEmpty(paraJsonArrayStr) && Objects.isNull(paraList)) {
                        resultObj = method.invoke(object);
                    } else if (StringUtils.isNotEmpty(paraJsonStr)) {
                        resultObj = method.invoke(object, JSON.parseObject(paraJsonStr, parameterTypes[0]));
                    } else if (StringUtils.isNotEmpty(paraJsonArrayStr)) {
                        resultObj = method.invoke(object, JSON.parseArray(paraJsonArrayStr, Object.class));
                    } else if (!Objects.isNull(paraList)) {
                        resultObj = method.invoke(object, paraList);
                    }
                } catch (Exception e) {
                    log.error("serviceName={} methodName={} 方法调用异常", serviceName, methodName);
                }
                break;
            }
        }
        return resultObj;
    }
}