package org.hf.boot.springboot.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/9/25 16:17
*/
@Getter
@Setter
@ToString
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = -5201854131689368150L;
    private Integer userId;
    private String userName;
    private String systemCode;
}