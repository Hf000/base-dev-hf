package org.hf.application.mybatis.multiple.datasource.mapper;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * <p> 公用Mapper </p>
 * 注解@Repository可加可不加, 加是防止idea报红
 * 整合通用mapper后实体mapper要继承通用mapper的mapper<T>接口
 * 该类不能放在tk.mybatis.spring.annotation.@MapperScan的扫描路径下, 否则会报类型转换异常
 * @author hufei
 * @date 2022/8/6 12:26
*/
@Repository
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
