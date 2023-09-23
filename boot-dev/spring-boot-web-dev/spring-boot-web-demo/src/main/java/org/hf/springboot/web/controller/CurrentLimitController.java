package org.hf.springboot.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.web.currentlimiting.guava.TokenBucketLimiter;
import org.hf.common.web.currentlimiting.semaphore.ShLimiter;
import org.hf.common.web.currentlimiting.sentinel.SentinelLimiter;
import org.hf.common.web.pojo.vo.ResponseVO;
import org.hf.common.web.utils.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 测试限流注解controller
 * @author HF
 */
@Slf4j
@RestController
@RequestMapping("current/limit")
public class CurrentLimitController {

    @GetMapping("sentinel")
    @SentinelLimiter(resourceName = "testCurrentLimit", limitCount = 5)
    public ResponseVO<Void> testSentinelLimiter() {
        log.info("SentinelLimiter限流接口测试");
        return ResponseUtil.success();
    }

    @GetMapping("semaphore")
    @ShLimiter(limitCount = 1)
    public ResponseVO<Void> testShLimiter() throws InterruptedException {
        log.info("ShLimiter限流接口测试");
        TimeUnit.SECONDS.sleep(2);
        return ResponseUtil.success();
    }

    @GetMapping("guava")
    @TokenBucketLimiter(limitCount = 3)
    public ResponseVO<Void> testTokenBucketLimiter() {
        log.info("TokenBucketLimiter限流接口测试");
        return ResponseUtil.success();
    }
}