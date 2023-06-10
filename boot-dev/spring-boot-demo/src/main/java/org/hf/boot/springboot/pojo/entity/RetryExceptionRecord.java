package org.hf.boot.springboot.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 表名：retry_exception_record
 * 表注释：重试异常记录
*/
@Getter
@Setter
@ToString
@Table(name = "retry_exception_record")
public class RetryExceptionRecord extends BaseEntity {
    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 业务编码
     */
    @Column(name = "business_code")
    private String businessCode;

    /**
     * 服务编码
     */
    @Column(name = "service_code")
    private String serviceCode;

    /**
     * 重试方式 METHOD或者URL
     */
    @Column(name = "retry_type")
    private String retryType;

    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount;

    /**
     * 重试状态:0-未重试,1-已重事,2-取消重试
     */
    @Column(name = "retry_state")
    private Byte retryState;

    /**
     * 异常信息
     */
    @Column(name = "exception_msg")
    private String exceptionMsg;

    /**
     * 请求信息,json存储
     */
    @Column(name = "request_info")
    private String requestInfo;
}