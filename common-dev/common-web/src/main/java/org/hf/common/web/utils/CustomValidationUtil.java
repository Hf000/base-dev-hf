package org.hf.common.web.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 自定义参数Validation工具类 </p>
 * 获取表单校验@Validated或@Valid错误信息
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 12:14
 */
public class CustomValidationUtil extends ValidationUtils {

    private CustomValidationUtil(){}

    /**
     * 拼接所有校验结果信息
     * @param bindingResult 绑定对象
     * @return 校验错误信息拼接字符串
     */
    public static String getValidatorResultDesc(BindingResult bindingResult) {
        // 获取所有的错误结果集合
        List<ValidatorResult> validatorResult = getValidatorResult(bindingResult);
        StringBuilder sbu = new StringBuilder();
        int i = 1;
        for (ValidatorResult result : validatorResult) {
            String tip = " %d: %s";
            sbu.append(String.format(tip, i , result.getMessage()));
            i++;
        }
        return sbu.toString();
    }

    /**
     * 获取某个对象全部字段数据校验的结果
     * @param bindingResult 绑定对象
     * @return 校验结果对象集合
     */
    public static List<ValidatorResult> getValidatorResult(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<ValidatorResult> results = new ArrayList<>();
        allErrors.forEach(error -> {
            // 获取类名称
            String objectName = error.getObjectName();
            // 获取校验的字段名称
            String field = ((FieldError) error).getField();
            // 获取提示信息
            String message = error.getDefaultMessage();
            results.add(new ValidatorResult(field, message));
        });
        return results;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ValidatorResult {
        /**
         * 字段名称
         */
        private String field;

        /**
         * 提示信息
         */
        private String message;
    }

}
