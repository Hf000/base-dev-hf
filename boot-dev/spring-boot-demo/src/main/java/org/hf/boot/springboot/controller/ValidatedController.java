package org.hf.boot.springboot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.pojo.dto.ReqParam;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.service.SaveValid;
import org.hf.boot.springboot.service.UpdatedValid;
import org.hf.boot.springboot.service.ValidatedService;
import org.hf.boot.springboot.service.impl.ValidatedServiceNewImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p> @Validated和@Valid请求示例 </p >
 * //@RequiredArgsConstructor 通过构造方法注入spring容器中的bean
 * @author hf
*/
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class ValidatedController {

    private final ValidatedService validatedService;

    private final ValidatedServiceNewImpl validatedServiceNewImpl;

    /**
     * controller层进行参数校验, 嵌套校验
     */
    @PostMapping(value = "/testValid")
    public Result<ReqParam> testValid(@Valid @RequestBody ReqParam req) {
        return Result.success(req);
    }

    /**
     * 验证在service层进行参数校验, 这里的@Validated和@Valid需要在service层加, 不能直接在实现类加, 因为这里在生成代理对象时需要能识别到校验注解
     */
    @PostMapping(value = "/testValidated")
    public Result<ReqParam> testValidated(@RequestBody ReqParam req) {
        return Result.success(validatedService.testValidated(req));
    }

    /**
     * 这里可以在实现类加@Validated和@Valid注解
     */
    @PostMapping(value = "/testValidated2")
    public Result<ReqParam> testValidated2(@RequestBody ReqParam req) {
        return Result.success(validatedServiceNewImpl.testValidated(req));
    }

    /**
     * 此方式可直接在具体参数前添加@NotNull...等校验注解, 类上需要加上@Validated
     */
    @PostMapping(value = "/testValidated3")
    public Result<Void> testValidated3(@RequestBody ReqParam req) {
        validatedService.testValidated3(req.getName());
        return new Result<>();
    }

    /**
     * 此方式可直接在具体参数前添加@NotNull...等校验注解, 类上需要加上@Validated
     */
    @PostMapping(value = "/testValidated4")
    public Result<ReqParam> testValidated4(@RequestBody ReqParam req) {
        validatedServiceNewImpl.testValidated4(req.getName());
        return new Result<>();
    }

    /**
     * 这里的@NotNull注解需要配合在类上加@Validated注解
     */
    @GetMapping(value = "/testReqParamValid")
    public Result<String> testReqParamValid(@NotNull(message = "参数不能为空") @RequestParam(value = "param", required = false) String param) {
        return Result.success(param);
    }

    /**
     * 分组校验
     */
    @PostMapping(value = "/testValidatedGroup")
    public Result<ReqParam> testValidatedGroup(@Validated(SaveValid.class) @RequestBody ReqParam req) {
        return Result.success(req);
    }

    /**
     * 分组校验
     */
    @PostMapping(value = "/testValidatedGroup2")
    public Result<ReqParam> testValidatedGroup2(@Validated(UpdatedValid.class) @RequestBody ReqParam req) {
        return Result.success(req);
    }

    /**
     * 自定义分组选择校验, 入参添加@GroupSequenceProvider(CustomValidatedGroupProvider.class)
     */
    @PostMapping(value = "/testValidatedGroup3")
    public Result<ReqParam> testValidatedGroup3(@Validated @RequestBody ReqParam req) {
        return Result.success(req);
    }
}