package org.hf.common.publi.interfac;

/**
 * <p> 响应码接口 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 19:45
 */
public interface StatusCode {

    /**
     * 提示码
     * @return 返回提示码
     */
    String getCode();

    /**
     * 提示信息
     * @return 返回提示信息
     */
    String getMsg();

}
