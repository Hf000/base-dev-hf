package org.hf.common.publi.pojo.dto;

import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p> 基础请求参数实体 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/7/30 15:55
 */
@Setter
@ToString
public class BasePageReq implements Serializable {
    private static final long serialVersionUID = 1246168034041179879L;

    /**
     * 默认页码
     */
    public static final Integer DEFAULT_PAGE_NO = 1;
    /**
     * 默认每页显示数量
     */
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 每页显示数量
     */
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo == null ? DEFAULT_PAGE_NO : pageNo;
    }

    public Integer getPageSize() {
        return pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
    }
}
