package org.hf.boot.springboot.service.impl;

import org.hf.boot.springboot.pojo.dto.ReqParam;
import org.hf.boot.springboot.service.ValidatedService;
import org.springframework.stereotype.Service;

/**
 * <p>  </p >
 *
 * @author hf
 * @date 2023-11-15
 **/
@Service
public class ValidatedServiceImpl implements ValidatedService {

    @Override
    public ReqParam testValidated(ReqParam req) {
        return req;
    }

    @Override
    public void testValidated3(String name) {

    }
}