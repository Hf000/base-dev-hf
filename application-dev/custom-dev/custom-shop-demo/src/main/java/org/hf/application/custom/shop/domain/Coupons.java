package org.hf.application.custom.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> 优惠券信息实体 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coupons implements Serializable {
    private static final long serialVersionUID = -4224100114117866988L;
    private String id;
    private String username;
    private Integer money;
    /**
     * 状态，1：未使用，2：已使用
     */
    private Integer status;
    /**
     * 用时间
     */
    private Date useTime;
}
