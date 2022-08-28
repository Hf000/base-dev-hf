package org.hf.springboot.web.pojo.vo;

import lombok.Data;
import org.hf.common.web.desensitize.Sensitive;
import org.hf.common.web.desensitize.SensitiveTypeEnum;

import java.io.Serializable;

/**
 * <p> 脱敏响应实体 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/28 19:21
 */
@Data
public class DesensitizeVO implements Serializable {

    private static final long serialVersionUID = -3909587909898024616L;

    private Long id;
    /**按照名字数据进行脱敏*/
    @Sensitive(type = SensitiveTypeEnum.CHINESE_NAME)
    private String name;

    /**按照邮箱数据进行脱敏 type指明脱敏类别*/
    @Sensitive(type = SensitiveTypeEnum.EMAIL)
    private String email;

    /**按照手机号数据进行脱敏 type指明脱敏类别*/
    @Sensitive(type = SensitiveTypeEnum.MOBILE_PHONE)
    private String phone;

    /**按照手机号数据进行脱敏 type指明脱敏类别*/
    @Sensitive(type = SensitiveTypeEnum.FIXED_PHONE)
    private String fixPhone;

    /**按照银行卡号数据进行脱敏 type指明脱敏类别*/
    @Sensitive(type = SensitiveTypeEnum.BANK_CARD)
    private String bankCard;

    /**按照地址数据进行脱敏 type指明脱敏类别*/
    @Sensitive(type = SensitiveTypeEnum.ADDRESS)
    private String dress;

    /**按照银行卡号数据进行脱敏 type指明脱敏类别*/
    @Sensitive(type = SensitiveTypeEnum.ID_CARD)
    private String idCard;

    @Sensitive(type = SensitiveTypeEnum.OFFICER_NUMBER)
    private String officerNumber;

    @Sensitive(type = SensitiveTypeEnum.PASSPORT)
    private String passport;

    @Sensitive(type = SensitiveTypeEnum.LICENCE)
    private String licence;

    @Sensitive(type = SensitiveTypeEnum.RESERVE_FUND)
    private String reserveFund;

    @Sensitive(type = SensitiveTypeEnum.SOCIAL_SECURITY)
    private String socialSecurity;

    @Sensitive(type = SensitiveTypeEnum.RESIDENCE_PERMIT)
    private String residencePermit;

    @Sensitive(type = SensitiveTypeEnum.MAC)
    private String mac;

    @Sensitive(type = SensitiveTypeEnum.IP)
    private String ip;

    @Sensitive(type = SensitiveTypeEnum.IMEI)
    private String iemi;

    @Sensitive(type = SensitiveTypeEnum.IDFA)
    private String idfa;

    @Sensitive(type = SensitiveTypeEnum.CAR_NO)
    private String carNo;

    @Sensitive(type = SensitiveTypeEnum.CAR_IDENTIFICATION_NUMBER)
    private String carIdentificationNumber;

    @Sensitive(type = SensitiveTypeEnum.CUSTOMER,prefixNoMaskLen=2,suffixNoMaskLen=3)
    private String description;

}
