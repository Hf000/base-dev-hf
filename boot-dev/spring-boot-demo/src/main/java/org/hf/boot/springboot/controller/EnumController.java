package org.hf.boot.springboot.controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hf.boot.springboot.pojo.dto.BaseEnumResp;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.service.impl.EnumFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 枚举相关接口 </p >
 *
 * @author hf
 * @date 2023-11-20
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("enum")
public class EnumController {

    private final EnumFactory enumFactory;

    @SuppressWarnings({"rawtypes"})
    @ApiOperation("获取枚举列表")
    @GetMapping("/getEnumList")
    public Result<List<BaseEnumResp>> getEnumList(@RequestParam("alias") String alias) {
        List<BaseEnumResp> baseEnumRespList = new ArrayList<>();
        List enumList = enumFactory.getList(alias);
        if (CollectionUtils.isNotEmpty(enumList)) {
            baseEnumRespList = BeanUtil.copyToList(enumList, BaseEnumResp.class);
        }
        return Result.success(baseEnumRespList);
    }
}