package org.hf.boot.springboot.dynamic.datasource;

import org.apache.commons.collections4.CollectionUtils;
import org.hf.boot.springboot.dao.DataSourceInfoMapper;
import org.hf.boot.springboot.pojo.dto.DataSourceDTO;
import org.hf.boot.springboot.pojo.entity.DataSourceInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * CommandLineRunner springboot项目在启动完成时需要预先加载的数据需要实现此接口,
 * 注意这里需要注入spring管理的bean, 所以需要加上@Component注解
 * 自定义多数据源处理 - 4
 */
@Component
public class LoadDataSourceRunner implements CommandLineRunner {

    @Resource
    private DynamicDataSource dynamicDataSource;

    @Resource
    private DataSourceInfoMapper dataSourceInfoMapper;

    @Override
    public void run(String... args) throws Exception {
        // 通过加载数据库配置的数据源信息创建数据源
        List<DataSourceInfo> dataSourceInfoList = dataSourceInfoMapper.selectAll();
        if (CollectionUtils.isNotEmpty(dataSourceInfoList)) {
            List<DataSourceDTO> ds = new ArrayList<>();
            for (DataSourceInfo dataSourceInfo : dataSourceInfoList) {
                DataSourceDTO sourceDto = new DataSourceDTO();
                BeanUtils.copyProperties(dataSourceInfo, sourceDto);
                sourceDto.setKey(dataSourceInfo.getName());
                ds.add(sourceDto);
            }
            dynamicDataSource.createDataSource(ds);
        }
    }
}