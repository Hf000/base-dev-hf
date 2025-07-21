package org.hf.boot.springboot.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.hf.boot.springboot.mapper.BaseMapper;
import org.hf.boot.springboot.pojo.dto.UserReq;
import org.hf.boot.springboot.pojo.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * 用户信息表
 * user_info
 * 
 * @author hufei
 * @date 2022/09/22 11:15
*/ 
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 流式导出测试
     */
    void streamExportTest(@Param("req") UserReq req, ResultHandler<UserInfo> handler);
}