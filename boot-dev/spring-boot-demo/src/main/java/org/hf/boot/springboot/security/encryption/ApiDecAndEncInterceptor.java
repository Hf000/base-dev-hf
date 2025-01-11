package org.hf.boot.springboot.security.encryption;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.filter.CustomFilter;
import org.hf.boot.springboot.pojo.dto.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口出入参加解密拦截器
 * 接口出入参加解密实现 - 2
 */
@Slf4j
@ControllerAdvice
public class ApiDecAndEncInterceptor implements HandlerInterceptor, ResponseBodyAdvice<Object> {

    @Autowired
    private ApiReqDecRespEncProperties apiReqDecRespEncProperties;

    /**
     * 拦截器前置方法
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        boolean isRequestWrapper = request instanceof CustomFilter.RequestWrapper;
        if (!isRequestWrapper) {
            log.info("不是指定类型的请求,直接放行");
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            log.info("请求不是指定的处理器类型,直接放行");
            return true;
        }
        ApiDecAndEnc apiDecAndEnc = ((HandlerMethod) handler).getMethod().getAnnotation(ApiDecAndEnc.class);
        if (apiDecAndEnc == null) {
            log.info("无需进行出入参加解密处理,直接放行");
            return true;
        }
        CustomFilter.RequestWrapper requestWrapper = (CustomFilter.RequestWrapper) request;
        String requestBody = requestWrapper.getRequestBody();
        ApiDecAndEncReq apiDecAndEncReq;
        try {
            apiDecAndEncReq = JSONObject.parseObject(requestBody, ApiDecAndEncReq.class);
        } finally {
            log.error("加密请求入参信息={}", requestBody);
        }
        if (apiDecAndEncReq == null) {
            throw new BusinessException("请求参数异常");
        } else {
            // 参数校验
            apiDecAndEncReq.checkParam();
        }
        // 设置当前加解密请求的上下文信息
        ApiDecAndEncDTO dto = new ApiDecAndEncDTO();
        dto.setApiDecAndEnc(apiDecAndEnc);
        dto.setEncReq(apiDecAndEncReq);
        ApiDecAndEncContext.addApiDecAndEncDto(dto);
        // TODO 根据apiDecAndEncReq.getAppId()校验当前应用是否在当前系统注册, 查询数据库
        /*String appId = null;
        // 如果未注册则提示则抛出异常当前请求不合法
        if (StringUtils.isBlank(appId)) {
            setResponse(response);
            return false;
        }*/
        String inPriKey = apiReqDecRespEncProperties.getInPriKey(apiDecAndEncReq.getAppId());
        String reqJsonData = ApiDecAndEncUtil.getDecryptData(apiDecAndEncReq, inPriKey);
        // 将解密后的请求数据设置到当前请求中
        requestWrapper.setRequestBody(reqJsonData);
        return true;
    }

    private void setResponse(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        Result<?> result = Result.fail("请求放没有在系统中进行注册");
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            throw new BusinessException("系统响应错误", e);
        }
    }

    /**
     * 拦截器完成时方法调用
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        // 拦截器完成时方法(在渲染视图解析器之后执行), 清除当前请求的加解密上下文信息
        ApiDecAndEncContext.removeContext();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 选择是否执行beforeBodyWrite, true-执行,否则不执行
     */
    @Override
    public boolean supports(MethodParameter returnType, @NotNull Class converterType) {
        // 校验带了指定注解的方法才进行拦截
        ApiDecAndEnc annotation = returnType.getAnnotatedElement().getAnnotation(ApiDecAndEnc.class);
        return annotation != null;
    }

    /**
     * 进行结果包装
     */
    @Override
    public Object beforeBodyWrite(Object respBody, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        if (respBody == null) {
            return Result.fail("数据为空");
        }
        if (!(respBody instanceof Result)) {
            return respBody;
        }
        Result<Object> result = JSONObject.parseObject(String.valueOf(respBody), new TypeReference<Result<Object>>() {
        });
        if (!result.isFlag()) {
            return result;
        }
        ApiDecAndEncDTO apiDecAndEncDto = ApiDecAndEncContext.getApiDecAndEncDto();
        if (apiDecAndEncDto == null || apiDecAndEncDto.getApiDecAndEnc() == null) {
            return result;
        }
        Object data = result.getData();
        if (data == null) {
            // 数据为空则直接返回
            return result;
        }
        ApiDecAndEncReq encReq = apiDecAndEncDto.getEncReq();
        String outPubKey = apiReqDecRespEncProperties.getOutPubKey(encReq.getAppId());
        // 不为空则加密数据
        ApiDecAndEncResp resultResp = ApiDecAndEncUtil.buildEncryptedData(JSON.toJSONString(data), outPubKey);
        result.setData(resultResp);
        return respBody;
    }
}