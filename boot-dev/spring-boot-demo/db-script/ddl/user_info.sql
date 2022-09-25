CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_name` varchar(100) NOT NULL COMMENT '用户账号',
  `password` varchar(100) NOT NULL COMMENT '用户密码',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `age` tinyint(3) DEFAULT NULL COMMENT '年龄',
  `sex` char(1) NOT NULL DEFAULT 'X' COMMENT '性别 M:男,F:女,X(默认值):未知',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `delete_state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除状态 0(默认值):正常,1:删除',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `updated_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';