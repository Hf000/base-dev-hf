package org.hf.springboot.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.web.pojo.vo.ResponseVO;
import org.hf.common.web.utils.ResponseUtil;
import org.hf.springboot.web.config.ConstantCacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p> 常量获取本地缓存支持 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 15:09
 */
@Slf4j
@RestController
@RequestMapping("support/constant")
public class ConstantController {

    @Autowired
    private ConstantCacheConfig constantCacheConfig;

    private static final String CONSTANT_PREFIX = "YEAR_";

    @GetMapping("number")
    public ResponseVO<List<Object>> getNumber() {
        Set<Object> values = constantCacheConfig.getConstantNumber().getValues(CONSTANT_PREFIX);
        List<Object> list = new ArrayList<>(values);
        list = list.stream().sorted().collect(Collectors.toList());
        return ResponseUtil.success(list);
    }

}
