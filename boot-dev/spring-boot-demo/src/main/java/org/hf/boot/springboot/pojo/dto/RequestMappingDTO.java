package org.hf.boot.springboot.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestMappingDTO {
    
    @ApiModelProperty("微服务")
    private String microSrv;
    
    @ApiModelProperty("package相对路径")
    private String relPackage;
    
    @ApiModelProperty("Controller名称")
    private String controller;
    
    @ApiModelProperty("接口分组名称")
    private String groupName;
    
    @ApiModelProperty("接口名称")
    private String urlResName;
    
    @ApiModelProperty("url映射")
    private String urlMapping;
    
    @ApiModelProperty("url模型")
    private String urlPattern;
    
    @ApiModelProperty("url访问权限检查")
    private Boolean urlAccessCheck;
    
    @ApiModelProperty("入参字段")
    private String inParamFields;
    
    @ApiModelProperty("出参字段")
    private String outParamFields;
}