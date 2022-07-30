package org.hf.common.publi.utils;

import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.constants.CommonConstant;

/**
 * <p> el表达式工具类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 8:31
 */
public class ElUtil {

    /**
     * 判断是否是el表达式
     * @param param 入参
     * @return 返回
     */
    public static boolean judgmentEl(String param) {
        if (StringUtils.isNotBlank(param)) {
            return StringUtils.startsWith(param, CommonConstant.DOLLAR_SIGN_BEFORE_BIG_PARANTHESES) && StringUtils.endsWith(param, CommonConstant.AFTER_BIG_PARANTHESES);
        }
        return false;
    }

    /**
     * 替换El表达式符号
     * @param param 入参
     * @return 返回
     */
    public static String replaceEl(String param) {
        if (StringUtils.isNotBlank(param)) {
            if (StringUtils.contains(param, CommonConstant.DOLLAR_SIGN) ) {
                param = StringUtils.replace(param, CommonConstant.DOLLAR_SIGN, CommonConstant.EMPTY_STR);
            }
            if (StringUtils.contains(param, CommonConstant.BEFORE_BIG_PARANTHESES) ) {
                param = StringUtils.replace(param, CommonConstant.BEFORE_BIG_PARANTHESES, CommonConstant.EMPTY_STR);
            }
            if (StringUtils.contains(param, CommonConstant.AFTER_BIG_PARANTHESES)) {
                param = StringUtils.replace(param, CommonConstant.AFTER_BIG_PARANTHESES, CommonConstant.EMPTY_STR);
            }
        }
        return param;
    }

}
