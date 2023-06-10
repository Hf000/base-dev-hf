package org.hf.boot.springboot.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author HUFEI
 * 自定义重试异常实现 - 8
 */
@Data
public class InvokeMethodReq implements Serializable {

    private static final long serialVersionUID = -4056350727035089993L;
    /**
     * 方法名
     */
    @NotBlank(message = "方法名称不能为空")
    private String methodName;
    /**
     * 服务名
     */
    @NotBlank(message = "方法所在的服务名称不能为空")
    private String serviceName;
    /**
     * json字符串
     */
    private String paraJsonStr;
    /**
     * 参数列表
     */
    private transient Object[] paraList;
    /**
     * json字符串，数组类型
     */
    private String paraJsonArrayStr;
}