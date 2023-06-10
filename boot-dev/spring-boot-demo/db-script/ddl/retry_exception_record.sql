CREATE TABLE `retry_exception_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `business_code` varchar(100) NOT NULL COMMENT '业务编码',
  `service_code` varchar(100) NOT NULL COMMENT '服务编码',
  `exception_msg` text NOT NULL COMMENT '异常信息',
  `request_info` text NOT NULL COMMENT '请求信息,json存储',
  `retry_type` varchar(50) NOT NULL COMMENT '重试方式 method或者url',
  `retry_count` int(5) DEFAULT NULL COMMENT '重试次数',
  `retry_state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '重试状态:0-未重试,1-已重事,2-取消重试',
  `delete_state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除状态 0(默认值):正常,1:删除',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_business_code` (`business_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重试异常记录';