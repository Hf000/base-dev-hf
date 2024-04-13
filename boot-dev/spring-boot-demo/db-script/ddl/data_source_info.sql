CREATE TABLE base_dev.`data_source_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `url` varchar(200) NOT NULL COMMENT '数据库地址',
  `username` varchar(100) NOT NULL COMMENT '数据库用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `driver_class_name` varchar(200) NOT NULL COMMENT '数据库驱动',
  `name` varchar(100) DEFAULT NULL COMMENT '数据库连接名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库连接信息';