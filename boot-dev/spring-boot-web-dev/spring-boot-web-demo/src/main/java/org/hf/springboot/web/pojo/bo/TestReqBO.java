package org.hf.springboot.web.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p> 请求入参 </p >
 * @author hufei
 * @date 2023-04-11
 **/
@Data
public class TestReqBO implements Serializable {

    private static final long serialVersionUID = -1L;

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