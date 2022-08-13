package org.hf.common.publi.interfac;

/**
 * <p> 响应接口 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 13:46
 */
public interface ResponseResult<T> {

    /**
     * 获取提示码
     * @return 提示码
     */
    String getResponseCode();

    /**
     * 获取提示信息
     * @return 提示信息
     */
    String getResponseMsg();

    /**
     * 获取提示数据
     * @return 提示数据
     */
    T getData();

}
