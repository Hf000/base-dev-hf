package org.hf.boot.springboot.dynamic.statement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DynamicStatementConfigDTO {

    @ApiModelProperty("接口编码")
    private String apiNo;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("描述")
    private String apiDesc;

    @ApiModelProperty("接口路径")
    private String mappingPath;

    @ApiModelProperty("接口调用类型GET、POST")
    private MappingMethodEnum methodEnum;

    @ApiModelProperty("接口返回内容类型")
    private String mappingProduces;

    @ApiModelProperty("请求入参")
    private List<String> requestParamsList;

    @ApiModelProperty("对应sql的statementId")
    private String statementId;

    @ApiModelProperty("参数配置")
    private List<ParameterDTO> parametersList;

    @ApiModelProperty("列配置")
    private List<ColumnDTO> columnsList;

    @ApiModelProperty("查询类型:SELECT_ONE-单行,SELECT_LIST-列表,SELECT_PAGE-分页")
    private SelectTypeEnum selectEnum;

    @ApiModelProperty("sql脚本")
    private String sqlScript;

    @ApiModelProperty("数据源key")
    private String dataSourceKey;

}