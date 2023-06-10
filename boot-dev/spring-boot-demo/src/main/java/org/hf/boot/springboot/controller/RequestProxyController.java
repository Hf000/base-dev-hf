package org.hf.boot.springboot.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hf.boot.springboot.pojo.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> 请求代理Controller实现 针对hub层实现 </p >
 * @author hufei
 * @date 2022/8/13 9:02
*/
@Api(tags = "请求代理")
@RestController
@RequestMapping("/proxyApi")
public class RequestProxyController {

    /*@Autowired
    private ProxyFeignClient proxyFeignClient;

    @ApiOperation(value = "通用代理转发")
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<Object> proxy(@RequestBody JSONObject requestBody, HttpServletRequest request) throws Exception {
        String servletPath = request.getServletPath();
        String path = servletPath.replace("/proxyApi/", "");
        return proxyFeignClient.proxy(path, requestBody);
    }*/
}