package org.hf.boot.springboot.dao;

import org.hf.boot.springboot.mapper.BaseMapper;
import org.hf.boot.springboot.pojo.entity.DataSourceInfo;
import org.springframework.stereotype.Repository;

/**
 * 数据源信息dao接口
 */
@Repository
public interface DataSourceInfoMapper extends BaseMapper<DataSourceInfo> {
}