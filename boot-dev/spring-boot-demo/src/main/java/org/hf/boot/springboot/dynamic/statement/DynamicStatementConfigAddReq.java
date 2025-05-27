package org.hf.boot.springboot.dynamic.statement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel("新增动态statement配置请求参数")
@Data
@NoArgsConstructor
public class DynamicStatementConfigAddReq {

    @NotBlank(message = "接口名称不能为空")
    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("描述")
    private String apiDesc;

    /**
     * 约定/data/api开头
     */
    @NotBlank(message = "url路径不能为空")
    @ApiModelProperty("接口路径")
    private String mappingPath;

    @ApiModelProperty("接口调用类型GET、POST")
    private MappingMethodEnum methodEnum;

    @ApiModelProperty("接口返回内容类型")
    private String mappingProduces;

    @ApiModelProperty("参数配置")
    private List<ParameterDTO> parametersList;

    @ApiModelProperty("列配置")
    private List<ColumnDTO> columnsList;

    @ApiModelProperty("查询类型:SELECT_ONE-单行,SELECT_LIST-列表,SELECT_PAGE-分页")
    private SelectTypeEnum selectEnum;

    @NotBlank(message = "sql脚本不能为空")
    @ApiModelProperty("sql脚本")
    private String sqlScript;

    @ApiModelProperty("数据源key")
    private String dataSourceKey = "master";

}