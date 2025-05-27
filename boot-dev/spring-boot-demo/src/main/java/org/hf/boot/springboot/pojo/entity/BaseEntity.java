package org.hf.boot.springboot.pojo.entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * <p> 数据库表对应实体基类 </p >
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/7/30 17:27
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -385145670322881254L;

    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdDate;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    private Date updatedDate;
    /**
     * 删除状态:0-正常,1-删除
     */
    @Column(name = "delete_state")
    private Byte deleteState;
}