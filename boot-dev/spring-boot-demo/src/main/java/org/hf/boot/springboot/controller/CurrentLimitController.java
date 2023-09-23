package org.hf.boot.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.currentlimiting.redis.RedisLimitAnnotation;
import org.hf.boot.springboot.pojo.dto.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试redis限流注解controller
 * @author HF
 */
@Slf4j
@RestController
@RequestMapping("current/limit")
public class CurrentLimitController {

    /**
     * TODO 待验证
     */
    @GetMapping("redisLua")
    @RedisLimitAnnotation(key = "testRedisLua", count = 5, period = 1)
    public Result<Void> testRedisLua() {
        log.info("RedisLua限流接口测试");
        return new Result<>();
    }
}