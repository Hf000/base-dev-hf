package org.hf.application.mybatis.multiple.datasource.dao;

import org.hf.application.mybatis.multiple.datasource.mapper.BaseMapper;
import org.hf.application.mybatis.multiple.datasource.pojo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p> 用户实体对应mapper </p>
 * //@Mapper    //如果启动类开启了@MapperScan注解，这里不需要这个注解了
 * 注解@Repository可加可不加 防止idea报红
 * //BaseMapper继承通用Mapper<T>和MySqlMapper<T> {@link BaseMapper}
 * @author hufei
 * @date 2022/8/6 12:28
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id查询用户信息
     * @param id 入参
     * @return 返回信息
     */
    User getUser(long id);

    /**
     * 查询用户信息列表
     * @return 返回信息
     */
    List<User> getUserAll();
}
