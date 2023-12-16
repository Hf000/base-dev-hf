package org.hf.boot.springboot.enumerate.mvc.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.collections4.CollectionUtils;
import org.hf.boot.springboot.enumerate.CommonEnum;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 枚举响应对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel(description = "通用枚举")
public class CommonEnumVO {

    @ApiModelProperty(notes = "Code")
    int code;

    @ApiModelProperty(notes = "Name")
    String name;

    @ApiModelProperty(notes = "描述")
    String desc;

    public static CommonEnumVO from(CommonEnum commonEnum){
        if (commonEnum == null){
            return null;
        }
        return new CommonEnumVO(commonEnum.getCode(), commonEnum.getName(), commonEnum.getDescription());
    }

    public static List<CommonEnumVO> from(List<CommonEnum> commonEnums){
        if (CollectionUtils.isEmpty(commonEnums)){
            return Collections.emptyList();
        }
        return commonEnums.stream()
                .filter(Objects::nonNull)
                .map(CommonEnumVO::from)
                .collect(Collectors.toList());
    }
}