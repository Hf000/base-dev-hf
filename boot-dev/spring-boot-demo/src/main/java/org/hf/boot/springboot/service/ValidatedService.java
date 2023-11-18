package org.hf.boot.springboot.service;

import org.hf.boot.springboot.pojo.dto.ReqParam;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>  </p >
 *
 * @author hf
 **/
@Validated
public interface ValidatedService {

    ReqParam testValidated(@Valid ReqParam req);

    void testValidated3(@NotNull(message = "name不能为空") String name);
}