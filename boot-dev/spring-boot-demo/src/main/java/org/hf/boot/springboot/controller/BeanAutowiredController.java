package org.hf.boot.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.service.BeanAutowiredService;
import org.hf.boot.springboot.service.impl.AbstractBeanAutowiredServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p> 测试bean注入 </p >
 **/
@Slf4j
@RestController
@RequestMapping("bean/autowired")
public class BeanAutowiredController {

    @Autowired
    private List<BeanAutowiredService> beanAutowiredServiceImplList;

    @Autowired
    private List<AbstractBeanAutowiredServiceImpl> abstractBeanAutowiredServiceImplList;

    @Autowired
    private Map<String, BeanAutowiredService> beanAutowiredServiceImplMap;

    @Autowired
    private Map<String, AbstractBeanAutowiredServiceImpl> abstractBeanAutowiredServiceImplMap;

    @GetMapping("queryBean")
    public Result<Void> queryBean() {
        log.info("查询bean的注入");
        System.out.println("bean的所有实现List" + beanAutowiredServiceImplList);
        System.out.println("===========================");
        System.out.println("bean的抽象所有实现List" + abstractBeanAutowiredServiceImplList);
        System.out.println("===========================");
        System.out.println("bean的所有实现Map" + beanAutowiredServiceImplMap);
        System.out.println("===========================");
        System.out.println("bean的抽象所有实现Map" + abstractBeanAutowiredServiceImplMap);
        return new Result<>();
    }
}