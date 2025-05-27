CREATE TABLE `dynamic_statement_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `api_no` varchar(50) NOT NULL COMMENT '接口编码',
  `api_name` varchar(50) NOT NULL COMMENT '接口名称',
  `api_desc` varchar(300) DEFAULT NULL COMMENT '描述',
  `mapping_path` varchar(300) NOT NULL COMMENT '接口路径',
  `mapping_method` varchar(50) NOT NULL COMMENT '接口调用类型',
  `mapping_produces` varchar(50) NOT NULL COMMENT '接口返回内容类型',
  `statement_id` varchar(50) NOT NULL COMMENT '对应sql的statementId',
  `parameters` text COMMENT 'sql入参配置',
  `columns` text COMMENT 'sql列配置',
  `select_type` varchar(50) NOT NULL COMMENT '查询类型:SELECT_ONE-单行,SELECT_LIST-列表,SELECT_PAGE-分页',
  `sql_script` text NOT NULL COMMENT 'sql脚本',
  `data_source_key` varchar(50) NOT NULL COMMENT '数据源key',
  `delete_state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除状态 0(默认值):正常,1:删除',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_api_no` (`api_no`),
  UNIQUE KEY `uk_path` (`mapping_path`),
  UNIQUE KEY `uk_statement_id` (`statement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态statement数据接口配置表'

INSERT INTO dynamic_statement_config
(api_no, api_name, api_desc, `mapping_path`, `mapping_method`, mapping_produces, statement_id, parameters, `columns`, select_type, `sql_script`, data_source_key, delete_state, created_by, created_date, updated_by, updated_date)
VALUES('API001', '测试方法', '测试方法', '/data/api/test', 'POST', 'application/json;charset=UTF-8', 'STATEMENT_ID0001', '[{"arrayFieldType":"int","description":"","fieldType":"array","in":"body","name":"userIds","required":false},{"arrayFieldType":"","description":"","fieldType":"date","in":"body","name":"queryDate","required":false},{"arrayFieldType":"","description":"","fieldType":"number","in":"body","name":"pageNum","required":true},{"arrayFieldType":"","description":"","fieldType":"number","in":"body","name":"pageSize","required":true}]', '[{"column":"id","description":"","fieldType":"int","name":"id"},{"column":"user_name","description":"","fieldType":"string","name":"userName"}]', 'SELECT_PAGE', 'select  id, user_name from user_info        <where>            <if test="userIds != null and userIds.size()>0">                and id in                <foreach collection="userIds" item="item" separator="," open="(" close=")">                    #{item}                </foreach>            </if>            <if test="queryDate != null">                and updated_date &gt; #{queryDate}            </if>            </where>', 'master', 0, 'system', now(), 'system', now());
