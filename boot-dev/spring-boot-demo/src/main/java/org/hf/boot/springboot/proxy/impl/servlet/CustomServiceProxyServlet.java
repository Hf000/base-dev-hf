package org.hf.boot.springboot.proxy.impl.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.proxy.servlet.AbstractProxyServlet;
import org.hf.boot.springboot.utils.RestTemplateUtil;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> 自定义实现代理servlet </p >
 * 继承AbstractProxyServlet 创建 CustomServiceProxyServlet ，实现具体服务请求体加密转换和响应体解密转换
 * 自定义服务代理实现 - 1
 * 业务根据具体情况实现AbstractProxyServlet，实现请求/响应体转换逻辑，并实例化bean
 * @author HUFEI
 * @date 2023-06-05
 **/
@Slf4j
public class CustomServiceProxyServlet extends AbstractProxyServlet {

    @SneakyThrows
    @Override
    protected Object doDispatch(HttpServletRequest servletRequest, Object requestJson) {
        String targetUri = (String) servletRequest.getAttribute(ATTR_TARGET_URI);
        log.info("CustomServiceProxyServlet.handler 调用CustomService url:{} 请求参数为：{}", targetUri, requestJson);
        Result<Object> baseResp = null;
        if (RequestMethod.GET.name().equals(servletRequest.getMethod())) {
            baseResp = getJson(targetUri, requestJson, new TypeReference<Result<Object>>() {});
        } else if (RequestMethod.POST.name().equals(servletRequest.getMethod())) {
            baseResp = postJson(targetUri, requestJson, new TypeReference<Result<Object>>() {});
        }
        log.info("CustomServiceProxyServlet.handler 调用CustomService 响应参数为：{}", baseResp);
        return baseResp;
    }

    @SneakyThrows
    public static <T> Result<T> postJson(String url, Object param, TypeReference<Result<T>> typeReference) {
        // 这里没做加接密, 实际情况一般需要进行加解密
        log.info("CustomServiceProxyServlet postJson url:{},param: {}", url, param);
        String result = RestTemplateUtil.postJson(url, param);
        log.info("CustomServiceProxyServlet postJson resp:{}:",result);
        return JSON.parseObject(result, typeReference);
    }

    @SneakyThrows
    public static <T> Result<T> getJson(String url, Object param, TypeReference<Result<T>> typeReference) {
        // 这里没做加接密, 实际情况一般需要进行加解密
        log.info("CustomServiceProxyServlet postJson url:{},param: {}", url, param);
        String result = RestTemplateUtil.getJson(url);
        log.info("CustomServiceProxyServlet postJson resp:{}:",result);
        return JSON.parseObject(result, typeReference);
    }
}