package org.hf.common.publi.utils;

import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.enums.ResponseEnum;
import org.hf.common.publi.exception.BusinessException;
import org.hf.common.publi.interfac.StatusCode;
import org.hf.common.publi.pojo.dto.ResponseDTO;

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
    public static ResponseDTO<Void> success() {
        return new ResponseDTO<>();
    }

    /**
     * 返回成功响应
     * @param statusCode 提示信息
     * @return 返回信息
     */
    public static ResponseDTO<Void> success(StatusCode statusCode) {
        ResponseDTO<Void> vo = success();
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
    public static <T> ResponseDTO<T> success(T obj) {
        ResponseDTO<T> vo = new ResponseDTO<>();
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
    public static <T> ResponseDTO<T> success(StatusCode statusCode, T obj) {
        ResponseDTO<T> vo = success(obj);
        if (Objects.nonNull(statusCode)) {
            vo.setResponseCode(statusCode.getCode());
            vo.setResponseMsg(statusCode.getMsg());
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
    public static <T> ResponseDTO<T> success(String responseMsg, T obj) {
        ResponseDTO<T> vo = success(obj);
        if (StringUtils.isNotBlank(responseMsg)) {
            vo.setResponseMsg(responseMsg);
        }
        return vo;
    }

    /**
     * 返回失败响应
     * @return 返回信息
     */
    public static ResponseDTO<Void> fail() {
        ResponseDTO<Void> vo = new ResponseDTO<>();
        vo.setResponseCode(ResponseEnum.FAIL.getCode());
        vo.setResponseMsg(ResponseEnum.FAIL.getMsg());
        return new ResponseDTO<>();
    }

    /**
     * 返回失败响应
     * @param statusCode 提示信息
     * @return 返回信息
     */
    public static ResponseDTO<Void> fail(StatusCode statusCode) {
        ResponseDTO<Void> vo = fail();
        if (Objects.nonNull(statusCode)) {
            vo.setResponseCode(statusCode.getCode());
            vo.setResponseMsg(statusCode.getMsg());
        }
        return new ResponseDTO<>();
    }

    /**
     * 返回失败响应
     * @param obj 数据
     * @param <T> 数据类型
     * @return 返回信息
     */
    public static <T> ResponseDTO<T> fail(T obj) {
        ResponseDTO<T> vo = new ResponseDTO<>();
        if (Objects.nonNull(obj)) {
            vo.setData(obj);
        }
        vo.setResponseCode(ResponseEnum.FAIL.getCode());
        vo.setResponseMsg(ResponseEnum.FAIL.getMsg());
        return vo;
    }

    /**
     * 返回失败响应
     * @param statusCode 提示信息
     * @param obj 数据
     * @param <T> 数据类型
     * @return 返回信息
     */
    public static <T> ResponseDTO<T> fail(StatusCode statusCode, T obj) {
        ResponseDTO<T> vo = fail(obj);
        if (Objects.nonNull(statusCode)) {
            vo.setResponseCode(statusCode.getCode());
            vo.setResponseMsg(statusCode.getMsg());
        }
        return vo;
    }

    /**
     * 返回失败响应
     * @param obj 返回数据
     * @param responseMsg 提示信息
     * @param <T> 数据类型
     * @return 返回信息
     */
    public static <T> ResponseDTO<T> fail(String responseMsg, T obj) {
        ResponseDTO<T> vo = fail(obj);
        if (StringUtils.isNotBlank(responseMsg)) {
            vo.setResponseMsg(responseMsg);
        }
        return vo;
    }

    /**
     * 判断接口响应成功并返回成功状态
     * @param responseDTO 响应结果
     * @param <T> 数据类型
     * @return true 成功
     */
    public static <T> boolean isOk(ResponseDTO<T> responseDTO) {
        if (responseDTO == null) {
            return false;
        }
        return ResponseEnum.SUCCESS.getCode().equals(responseDTO.getResponseCode());
    }

    /**
     * 判断接口响应成功并返回成功状态, 且返回数据结果不为空
     * @param responseDTO 响应结果
     * @param <T> 数据类型
     * @return true 成功
     */
    public static <T> boolean isOkDataNotNull(ResponseDTO<T> responseDTO) {
        if (isOk(responseDTO)) {
            return responseDTO.getData() != null;
        }
        return false;
    }

    /**
     * 判断接口响应成功并返回成功状态, 且返回数据结果为空
     * @param responseDTO 响应结果
     * @param <T> 数据类型
     * @return true 成功
     */
    public static <T> boolean isOkDataNull(ResponseDTO<T> responseDTO) {
        if (isOk(responseDTO)) {
            return responseDTO.getData() == null;
        }
        return false;
    }

    /**
     * 判断接口响应成功并返回失败状态
     * @param responseDTO 响应结果
     * @param <T> 数据类型
     * @return true 成功
     */
    public static <T> boolean isFailed(ResponseDTO<T> responseDTO) {
        return !isOk(responseDTO);
    }

    /**
     * 判断接口响应成功并返回失败状态, 抛出异常
     * @param responseDTO 响应结果
     * @param <T> 数据类型
     */
    public static <T> void isFailedException(ResponseDTO<T> responseDTO) {
        if (responseDTO == null) {
            throw new BusinessException(ResponseEnum.FAIL);
        } else if (!ResponseEnum.SUCCESS.getCode().equals(responseDTO.getResponseCode())) {
            throw new BusinessException(responseDTO.getResponseMsg());
        } else if (responseDTO.getData() == null) {
            throw new BusinessException(ResponseEnum.NULL_DATA);
        }
        throw new BusinessException();
    }

}
