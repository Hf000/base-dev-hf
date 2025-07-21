package org.hf.boot.springboot.pojo.dto;

import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p> 基础请求参数实体 </p>
 * @author HF
 */
@Setter
@ToString
public class BasePageReq implements Serializable {

    private static final long serialVersionUID = -7100773154015049303L;

    /**
     * 默认页码
     */
    public static final Integer DEFAULT_PAGE_NUM = 1;
    /**
     * 默认每页显示数量
     */
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页显示数量
     */
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum == null ? DEFAULT_PAGE_NUM : pageNum;
    }

    public Integer getPageSize() {
        return pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
    }
}
