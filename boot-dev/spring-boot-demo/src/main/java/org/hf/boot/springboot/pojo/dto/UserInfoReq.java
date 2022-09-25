package org.hf.boot.springboot.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/9/25 16:22
*/
@Getter
@Setter
@ToString
public class UserInfoReq extends BaseDataDTO {
    private static final long serialVersionUID = 3009416783961800428L;
    private Integer id;
    private String userName;
    private String password;
    private String name;
    private Byte age;
    private String sex;
    private Date birthday;
}