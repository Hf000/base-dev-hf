package org.hf.boot.springboot.pojo.dto;

import lombok.Data;
import org.hf.boot.springboot.service.SaveValid;
import org.hf.boot.springboot.service.UpdatedValid;
import org.hf.boot.springboot.service.impl.CustomValidatedGroupProvider;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@GroupSequenceProvider(CustomValidatedGroupProvider.class)
public class ReqParam {

    @NotNull(message = "不能为空")
    private Integer userId;

    private String name;

    @Valid
    private ReqSubParam reqSubParam;

    @Data
    private static class ReqSubParam {
        @NotNull
        private String age;
    }

    @NotNull(message = "保存不能为空", groups = {SaveValid.class})
    private String groupSaveParam;

    @NotNull(message = "更新不能为空", groups = UpdatedValid.class)
    private String groupUpdateParam;

    private String groupParam;
}