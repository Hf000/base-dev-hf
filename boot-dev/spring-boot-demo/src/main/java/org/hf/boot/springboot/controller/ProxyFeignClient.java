package org.hf.boot.springboot.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.hf.boot.springboot.pojo.dto.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p> feign接口实现 </p >
 * @author HUFEI
 * @date 2023-06-06
 **/
// @FeignClient(name = "service-name", contextId = "proxyFeignClient", path = "/proxyApi")
public interface ProxyFeignClient {

    @ApiOperation("proxyApi统一代理方法")
    @PostMapping("/{path}")
    Result<Object> proxy(@PathVariable("path") String path, @RequestBody JSONObject requestBody);
}