package org.hf.boot.springboot.service.impl;

import org.hf.boot.springboot.pojo.dto.ReqParam;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>  </p >
 *
 * @author hf
 * @date 2023-11-15
 **/
@Service
@Validated
public class ValidatedServiceNewImpl {

    public ReqParam testValidated(@Valid ReqParam req) {
        return req;
    }

    public void testValidated4(@NotNull(message = "name不能为空") String name) {

    }
}