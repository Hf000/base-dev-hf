package org.hf.boot.springboot.controller;

import org.hf.boot.springboot.config.EventPublisher;
import org.hf.boot.springboot.pojo.bo.TestEvent;
import org.hf.boot.springboot.pojo.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 事件controller </p >
 *
 * @author hufei
 * @date 2022-09-21
 **/
@RestController
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventPublisher eventPublisher;

    @GetMapping("/test")
    public Result<Void> test() {
        TestEvent testEvent = new TestEvent();
        testEvent.setName("事件测试");
        eventPublisher.publishEvent(testEvent);
        return new Result<>();
    }

}