package org.hf.boot.springboot.dao;

import org.hf.boot.springboot.pojo.entity.RetryExceptionRecord;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * 重试异常记录
 * retry_exception_record
 * 自定义重试异常实现 - 5
 * @author HUFEI
 * @date 2023/06/07 10:59
*/ 
@Repository
public interface RetryExceptionRecordMapper extends Mapper<RetryExceptionRecord> {
}