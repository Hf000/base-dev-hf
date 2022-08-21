package org.hf.springcloud.provider.service.mapper;

import org.hf.springcloud.provider.service.pojo.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * <p> userMapper接口 </p>
 * @author hufei
 * @date 2022/8/21 17:39
*/
@Repository
public interface UserMapper extends Mapper<User> {
}
