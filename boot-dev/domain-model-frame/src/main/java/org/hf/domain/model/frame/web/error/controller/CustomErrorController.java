package org.hf.domain.model.frame.web.error.controller;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.web.pojo.vo.ResponseVO;
import org.hf.common.web.utils.ResponseUtil;
import org.hf.domain.model.frame.web.error.enums.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 处理404等异常 springBoot默认异常处理机制:org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
 */
@ApiIgnore
@Slf4j
@Controller
@RequestMapping("${server.error.path:/error}")
public class CustomErrorController extends AbstractErrorController {
    
    private final ErrorProperties errorProperties;
    
    @Autowired
    public CustomErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes);
        this.errorProperties = serverProperties.getError();
    }

    /**
     * 如果发送的数据请求头中有:Accept:text/html，则优先处理这个请求的数据
     */
    @RequestMapping(produces = {MediaType.TEXT_HTML_VALUE})
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = this.getStatus(request);
        log.warn("error:{}", status);
        Map<String, Object> model = Collections.unmodifiableMap(this.getErrorAttributes(request, this.getErrorAttributeOptions(request)));
        response.setStatus(status.value());
        ModelAndView modelAndView = this.resolveErrorView(request, response, status, model);
        return modelAndView != null ? modelAndView : new ModelAndView("error", model);
    }

    /**
     * 指定consumes为application/json,那么服务器仅处理request的Content-Type为application/json类型的请求
     */
    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseVO<String> error(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> body = getErrorAttributes(request, this.getErrorAttributeOptions(request));
        HttpStatus status = getStatus(request);
        log.warn("error:{} Exception:{}, Message:{}", body.get("error"), body.get("exception"), body.get("message"));
        if (status == HttpStatus.NOT_FOUND) {
            return ResponseUtil.fail(ExceptionEnum.NOT_FOUND, "接口不存在");
        }
        return ResponseUtil.fail(ExceptionEnum.REQ_ERROR.getCode(), (String) body.get("trace"));
    }

    /**
     * 获取错误属性
     */
    protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request) {
        ErrorProperties.IncludeAttribute include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeAttribute.ALWAYS) {
            return ErrorAttributeOptions.of(ErrorAttributeOptions.defaults().getIncludes());
        }
        if (include == ErrorProperties.IncludeAttribute.ON_PARAM) {
            boolean isIncludeStackTrace = getTraceParameter(request);
            return isIncludeStackTrace ? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE) :
                    ErrorAttributeOptions.of(ErrorAttributeOptions.defaults().getIncludes());
        }
        return ErrorAttributeOptions.defaults();
    }

    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

}