package org.hf.common.web.utils;

import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.interfac.StatusCode;
import org.hf.common.web.enums.ResponseEnum;
import org.hf.common.web.pojo.vo.ResponseVO;

import java.util.Objects;

/**
 * <p> 响应工具类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 12:23
 */
public class ResponseUtil {

    /**
     * 返回成功响应
     * @return 返回信息
     */
    public static ResponseVO<Void> success() {
        return new ResponseVO<>();
    }

    /**
     * 返回成功响应
     * @param statusCode 提示信息
     * @return 返回信息
     */
    public static ResponseVO<Void> success(StatusCode statusCode) {
        ResponseVO<Void> vo = new ResponseVO<>();
        if (Objects.nonNull(statusCode)) {
            vo.setResponseCode(statusCode.getCode());
            vo.setResponseMsg(statusCode.getMsg());
        }
        return vo;
    }

    /**
     * 返回成功响应
     * @param obj  返回数据
     * @param <T> 参数类型
     * @return 返回信息
     */
    public static <T> ResponseVO<T> success(T obj) {
        ResponseVO<T> vo = new ResponseVO<>();
        if (Objects.nonNull(obj)) {
            vo.setData(obj);
        }
        return vo;
    }

    /**
     * 返回成功响应
     * @param statusCode 提示信息
     * @param obj 返回数据
     * @param <T> 数据类型
     * @return 返回信息
     */
    public static <T> ResponseVO<T> success(StatusCode statusCode, T obj) {
        ResponseVO<T> vo = new ResponseVO<>();
        if (Objects.nonNull(statusCode)) {
            vo.setResponseCode(statusCode.getCode());
            vo.setResponseMsg(statusCode.getMsg());
        }
        if (Objects.nonNull(obj)) {
            vo.setData(obj);
        }
        return vo;
    }

    /**
     * 返回成功响应
     * @param responseMsg 提示信息
     * @param obj 返回数据
     * @param <T> 数据类型
     * @return 返回信息
     */
    public static <T> ResponseVO<T> success(String responseMsg, T obj) {
        ResponseVO<T> vo = new ResponseVO<>();
        if (StringUtils.isNotBlank(responseMsg)) {
            vo.setResponseMsg(responseMsg);
        }
        if (Objects.nonNull(obj)) {
            vo.setData(obj);
        }
        return vo;
    }

    /**
     * 返回失败响应
     * @return 返回信息
     */
    public static ResponseVO<Void> fail() {
        ResponseVO<Void> vo = new ResponseVO<>();
        vo.setResponseCode(ResponseEnum.FAIL.getCode());
        vo.setResponseMsg(ResponseEnum.FAIL.getMsg());
        return new ResponseVO<>();
    }

    /**
     * 返回失败响应
     * @param obj 数据
     * @param <T> 数据类型
     * @return 返回信息
     */
    public static <T> ResponseVO<T> fail(T obj) {
        ResponseVO<T> vo = new ResponseVO<>();
        if (Objects.nonNull(obj)) {
            vo.setData(obj);
        }
        vo.setResponseCode(ResponseEnum.FAIL.getCode());
        vo.setResponseMsg(ResponseEnum.FAIL.getMsg());
        return vo;
    }

    /**
     * 返回失败响应
     * @param obj 返回数据
     * @param responseMsg 提示信息
     * @param <T> 数据类型
     * @return 返回信息
     */
    public static <T> ResponseVO<T> fail(T obj, String responseMsg) {
        ResponseVO<T> vo = new ResponseVO<>();
        if (Objects.nonNull(obj)) {
            vo.setData(obj);
        }
        vo.setResponseCode(ResponseEnum.FAIL.getCode());
        if (StringUtils.isNotBlank(responseMsg)) {
            vo.setResponseMsg(responseMsg);
        } else {
            vo.setResponseMsg(ResponseEnum.FAIL.getMsg());
        }
        return vo;
    }

}
