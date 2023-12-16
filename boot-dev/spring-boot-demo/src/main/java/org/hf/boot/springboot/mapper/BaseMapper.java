package org.hf.boot.springboot.mapper;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * <p> 公用Mapper </p>
 * 1.注解@Repository可加可不加, 加是防止idea报红
 * 2.整合通用mapper后实体mapper要继承通用mapper的mapper<T>接口,提供了一些常见的增删改查方法; 这里也继承了MySqlMapper接口,提供了针对mysql特性的一些方法
 * 3.该类不能放在tk.mybatis.spring.annotation.@MapperScan的扫描路径下, 否则会报类型转换异常
 * 4.这里加@Mapper注解,是防止启动报警告
 * @author hufei
 * @date 2022/8/6 12:26
*/
@org.apache.ibatis.annotations.Mapper
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
