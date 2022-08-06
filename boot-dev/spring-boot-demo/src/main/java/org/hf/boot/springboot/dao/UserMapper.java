package org.hf.boot.springboot.dao;

import org.hf.boot.springboot.mapper.BaseMapper;
import org.hf.boot.springboot.pojo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author:hufei
 * @CreateTime:2020-09-09
 * @Description:
 */
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
    User getUser(long id);

    List<User> getUserAll();
}
