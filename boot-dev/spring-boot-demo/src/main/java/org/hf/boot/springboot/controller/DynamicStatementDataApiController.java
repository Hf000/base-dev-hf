package org.hf.boot.springboot.controller;

import com.alibaba.fastjson2.JSONObject;
import org.hf.boot.springboot.dynamic.statement.DynamicStatementConfigAddReq;
import org.hf.boot.springboot.dynamic.statement.DynamicStatementConfigService;
import org.hf.boot.springboot.dynamic.statement.DynamicStatementDataApiService;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.pojo.entity.DynamicStatementConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/data/api")
public class DynamicStatementDataApiController {

    @Autowired
    private DynamicStatementDataApiService dynamicConfigService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private DynamicStatementConfigService dynamicStatementConfigService;

    public Result<Object> postJson(@RequestBody JSONObject parameter){
        String uri = httpServletRequest.getRequestURI();
        Object result = dynamicConfigService.postJson(uri,parameter);
        return Result.success(result);
    }

    @GetMapping(value = "/getDataApiConfigList")
    public Result<List<DynamicStatementConfig>> getDataApiConfigList() {
        return Result.success(dynamicStatementConfigService.getDataApiConfigList());
    }

    @PostMapping(value = "/addConfigInfo")
    public Result<Void> addDataApiConfig(@RequestBody @Valid DynamicStatementConfigAddReq req) {
        dynamicStatementConfigService.addDynamicStatementConfig(req);
        return Result.success(null);
    }

}