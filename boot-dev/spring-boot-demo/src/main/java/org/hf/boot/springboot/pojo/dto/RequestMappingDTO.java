package org.hf.boot.springboot.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestMappingDTO {

    @ExcelProperty(value = "微服务", index = 0)
    @ApiModelProperty("微服务")
    private String microSrv;

    @ExcelProperty(value = "package相对路径", index = 1)
    @ApiModelProperty("package相对路径")
    private String relPackage;

    @ExcelProperty(value = "Controller名称", index = 2)
    @ApiModelProperty("Controller名称")
    private String controller;

    @ExcelProperty(value = "接口分组名称", index = 3)
    @ApiModelProperty("接口分组名称")
    private String groupName;

    @ExcelProperty(value = "接口名称", index = 4)
    @ApiModelProperty("接口名称")
    private String urlResName;

    @ExcelProperty(value = "url映射", index = 5)
    @ApiModelProperty("url映射")
    private String urlMapping;

    @ExcelProperty(value = "url模型", index = 6)
    @ApiModelProperty("url模型")
    private String urlPattern;

    @ExcelProperty(value = "url访问权限检查", index = 7)
    @ApiModelProperty("url访问权限检查")
    private Boolean urlAccessCheck;

    @ExcelProperty(value = "入参字段", index = 8)
    @ApiModelProperty("入参字段")
    private String inParamFields;

    @ExcelProperty(value = "出参字段", index = 9)
    @ApiModelProperty("出参字段")
    private String outParamFields;

    @ExcelProperty(value = "出参包含的指定字段", index = 10)
    @ApiModelProperty("出参包含的指定字段")
    private String outParamCustomFields;

    @ExcelProperty(value = "入参包含的指定字段", index = 11)
    @ApiModelProperty("入参包含的指定字段")
    private String inParamCustomFields;
}