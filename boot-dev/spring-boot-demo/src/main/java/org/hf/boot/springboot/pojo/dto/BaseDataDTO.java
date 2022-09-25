package org.hf.boot.springboot.pojo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> 基础传输数据类实体 </p >
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/7/30 15:42
 */
@Data
public class BaseDataDTO implements Serializable {

    private static final long serialVersionUID = 6824954909587376078L;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 创建日期
     */
    private Date createdDate;
    /**
     * 更新日期
     */
    private Date updatedDate;

    /**
     * 删除状态:0-正常,1-删除
     */
    private Byte deleteState;

}