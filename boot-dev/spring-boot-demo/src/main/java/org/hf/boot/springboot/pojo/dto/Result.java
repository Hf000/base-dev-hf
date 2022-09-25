package org.hf.boot.springboot.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hf.boot.springboot.constants.StatusCode;

/**
 * <p> 返回实体Bean </p>
 *
 * @author hufei
 * @date 2022/8/7 17:14
 */
@ApiModel(description = "Result", value = "Result")
public class Result<T> {

    @ApiModelProperty(value = "执行是否成功,true:成功,false:失败", required = true)
    private boolean flag;
    @ApiModelProperty(value = "返回状态码,20000:成功,20001:失败,20002:用户名或密码错误,20003:权限不足,20004:远程调用失败,20005:重复操作,20006:没有对应的数据", required = true)
    private Integer code;
    @ApiModelProperty(value = "提示信息", required = true)
    private String message;
    @ApiModelProperty(value = "逻辑数据", required = true)
    private T data;

    public Result(boolean flag, Integer code, String message, T data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result() {
        this.flag = true;
        this.code = StatusCode.OK;
        this.message = "操作成功!";
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(T t) {
        Result<T> result = new Result<>();
        result.setData(t);
        return result;
    }

    public static <T> Result<?> fail(T t) {
        Result<T> result = new Result<>(false, StatusCode.ERROR, "系统错误");
        result.setData(t);
        return result;
    }

}
