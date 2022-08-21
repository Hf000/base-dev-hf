package org.hf.springcloud.provider.service.two.mapper;

import org.hf.springcloud.provider.service.two.pojo.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * <p> userMapper接口 </p>
 * @author hufei
 * @date 2022/8/21 17:49
*/
@Repository
public interface UserMapper extends Mapper<User> {
}
