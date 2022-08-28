package org.hf.common.publi.utils;

import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.constants.CommonConstant;

import static org.hf.common.publi.constants.CommonConstant.AFTER_BIG_BRACKETS;
import static org.hf.common.publi.constants.CommonConstant.BEFORE_BIG_BRACKETS;
import static org.hf.common.publi.constants.CommonConstant.DOLLAR_SIGN;
import static org.hf.common.publi.constants.CommonConstant.EMPTY_STR;

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
            return StringUtils.startsWith(param, DOLLAR_SIGN + BEFORE_BIG_BRACKETS) && StringUtils.endsWith(param, AFTER_BIG_BRACKETS);
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
            if (StringUtils.contains(param, DOLLAR_SIGN) ) {
                param = StringUtils.replace(param, DOLLAR_SIGN, EMPTY_STR);
            }
            if (StringUtils.contains(param, BEFORE_BIG_BRACKETS) ) {
                param = StringUtils.replace(param, BEFORE_BIG_BRACKETS, EMPTY_STR);
            }
            if (StringUtils.contains(param, AFTER_BIG_BRACKETS)) {
                param = StringUtils.replace(param, AFTER_BIG_BRACKETS, EMPTY_STR);
            }
        }
        return param;
    }

}
