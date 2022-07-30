package org.hf.common.publi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

/**
 * <p> base64工具类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 12:23
 */
@Slf4j
public class Base64Utils {

    /**
     * 将byte[] 转换成 String
     * @param bytes 需要转换的 byte[]
     * @return 如果返回结果为 "" 则转换失败, 否则成功
     */
    public static String byteToString(byte[] bytes) {
        try {
            //传入参数异常, 转换异常
            if (bytes == null || bytes.length < 1) {
                return "";
            }
            //将byte数据转换为字符串 并返回
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error("byte[] 转换 String 失败!!!", e);
        }
        return "";
    }

    /**
     * 将 String 转换成 byte[]
     * @param content 需要转换的 String
     * @return 如果返回结果为 "" 则转换失败, 否则成功
     */
    public static byte[] stringToByte(String content) {
        try {
            //传入参数异常, 转换异常
            if (StringUtils.isEmpty(content)) {
                return null;
            }
            //将byte数据转换为字符串 并返回
            return Base64.getDecoder().decode(content);
        } catch (Exception e) {
            log.error("String 转换 byte[] 失败!!!", e);
        }
        return null;
    }
}
