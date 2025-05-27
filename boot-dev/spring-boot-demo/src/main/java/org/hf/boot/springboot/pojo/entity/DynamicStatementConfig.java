package org.hf.boot.springboot.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hf.boot.springboot.dynamic.statement.MappingMethodEnum;
import org.hf.boot.springboot.dynamic.statement.SelectTypeEnum;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = false)
@Data
@Table(name = "dynamic_statement_config")
public class DynamicStatementConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ApiModelProperty("自增主键")
    private Integer id;

    @Column(name = "api_no")
    @ApiModelProperty("接口编码")
    private String apiNo;

    @Column(name = "api_name")
    @ApiModelProperty("接口名称")
    private String apiName;

    @Column(name = "api_desc")
    @ApiModelProperty("描述")
    private String apiDesc;

    @Column(name = "mapping_path")
    @ApiModelProperty("接口路径")
    private String mappingPath;

    /**
     * {@link MappingMethodEnum}
     */
    @Column(name = "mapping_method")
    @ApiModelProperty("接口调用类型GET、POST")
    private String mappingMethod;

    @Column(name = "mapping_produces")
    @ApiModelProperty("接口返回内容类型")
    private String mappingProduces;

    @Column(name = "statement_id")
    @ApiModelProperty("对应sql的statementId")
    private String statementId;

    @ApiModelProperty("参数配置")
    private String parameters;

    @ApiModelProperty("列配置")
    private String columns;

    /**
     * {@link SelectTypeEnum}
     */
    @Column(name = "select_type")
    @ApiModelProperty("查询类型:SELECT_ONE-单行,SELECT_LIST-列表,SELECT_PAGE-分页")
    private String selectType;

    @Column(name = "sql_script")
    @ApiModelProperty("sql脚本")
    private String sqlScript;

    @Column(name = "`data_source_key`")
    @ApiModelProperty("数据源key")
    private String dataSourceKey;

}