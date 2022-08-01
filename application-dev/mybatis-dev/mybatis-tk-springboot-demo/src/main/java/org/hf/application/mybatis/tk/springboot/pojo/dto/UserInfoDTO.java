package org.hf.application.mybatis.tk.springboot.pojo.dto;

import lombok.Setter;
import lombok.Getter;
import java.util.Date;
import lombok.ToString;
import org.hf.common.publi.pojo.dto.BaseDataDTO;

@Getter
@Setter
@ToString
public class UserInfoDTO extends BaseDataDTO {
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private String name;
    private Integer age;
    private Byte sex;
    private Date birthday;
    private String note;

}
