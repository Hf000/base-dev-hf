package org.hf.boot.springboot.security.sign;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.error.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 接口验签拦截器
 * 接口签名实现 - 2
 */
@Slf4j
@Component
public class ApiSignVerificationInterceptor implements HandlerInterceptor {

    /**
     * 给接口调用方生成指定的请求key用于验签生成
     */
    @Value("${request.api.key:default}")
    private String requestKey;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            log.info("请求不是指定的处理器类型,直接放行");
            return true;
        }
        ApiSignVerification apiVerification = ((HandlerMethod) handler).getMethod()
                .getAnnotation(ApiSignVerification.class);
        if (apiVerification == null) {
            log.info("处理器执行方法没有指定注解,直接放行");
            return true;
        }
        // 获取时间戳
        String timestampStr = request.getHeader("timestamp");
        if (StringUtils.isBlank(timestampStr)) {
            timestampStr = request.getParameter("timestamp");
        }
        // 判断时间是否大于xx秒(防止重放攻击)
        long timestamp;
        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (NumberFormatException e) {
            throw new BusinessException("错误的时间戳");
        }
        long nowMillis = System.currentTimeMillis();
        long timeDiff = nowMillis - timestamp;
        if (timeDiff < 0 || timeDiff > apiVerification.expiryTime()) {
            throw new BusinessException("失效的时间戳");
        }
        /**
         * 获取随机字符串 可以根据此参数进行接口防重,在Redis中设置redisTemplate.opsForValue().setIfAbsent(nonceStr, nonceStr,
         * expiredInMilliSeconds, TimeUnit.MILLISECONDS), 返回true则通过,否则为重复请求
         */
        String nonceStr = request.getHeader("nonceStr");
        if (StringUtils.isBlank(nonceStr)) {
            nonceStr = request.getHeader("nonceStr");
        }
        // 获取签名
        String signature = request.getHeader("signature");
        if (StringUtils.isBlank(signature)) {
            signature = request.getHeader("signature");
        }
        // 对请求头参数进行签名
        if (StrUtil.isEmpty(signature) || !Objects.equals(signature, this.genSign(timestampStr, nonceStr, request))) {
            throw new BusinessException("验签失败");
        }
        return true;
    }

    /**
     * 生成sign
     * @param timestamp 时间戳
     * @param nonceStr  随机串
     * @param request   请求信息
     * @return  签名
     */
    private String genSign(String timestamp, String nonceStr, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>(16);
        Enumeration<String> enumeration = request.getParameterNames();
        if (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getParameter(name);
            try {
                params.put(name, URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                log.error("验签时参数={}的值编码失败", name, e);
            }
        }
        String buildStr = String.format("%s&tamp=%s&nonceStr=%s&key=%s", sortQueryParamString(params), timestamp,
                nonceStr, requestKey);
        String sign = SecureUtil.md5(buildStr).toLowerCase();
        log.info("sign:{}", sign);
        return sign;
    }

    /**
     * 将请求参数按照字母顺序进行升序排序
     * @param params 请求参数
     * @return 排序后结果
     */
    private String sortQueryParamString(Map<String, Object> params) {
        List<String> paramNames = new ArrayList<>(params.keySet());
        Collections.sort(paramNames);
        StrBuilder content = StrBuilder.create();
        for (String param : paramNames) {
            content.append(param).append("=").append(params.get(param).toString()).append("&");
        }
        if (content.length() > 0) {
            return content.subString(0, content.length() - 1);
        }
        return content.toString();
    }
}