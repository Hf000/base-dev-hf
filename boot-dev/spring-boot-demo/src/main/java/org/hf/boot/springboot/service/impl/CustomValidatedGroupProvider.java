package org.hf.boot.springboot.service.impl;

import org.hf.boot.springboot.pojo.dto.ReqParam;
import org.hf.boot.springboot.service.UpdatedValid;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> @Validated注解自定义分组校验 </p >
 *
 * @author hf
 * @date 2023-11-16
 **/
public class CustomValidatedGroupProvider implements DefaultGroupSequenceProvider<ReqParam> {

    @Override
    public List<Class<?>> getValidationGroups(ReqParam reqParam) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(ReqParam.class);
        if (reqParam != null) {
            if ("updated".equals(reqParam.getGroupParam())) {
                defaultGroupSequence.add(UpdatedValid.class);
            }
        }
        return defaultGroupSequence;
    }
}