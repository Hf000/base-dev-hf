package org.hf.boot.springboot.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/9/25 16:17
*/
@Getter
@Setter
@ToString
public class UserInfoDTO extends BaseDataDTO {
    private static final long serialVersionUID = -5201854131689368150L;
    private Integer id;
    private String userName;
    private String password;
    private String name;
    private Byte age;
    private String sex;
    private Date birthday;

}