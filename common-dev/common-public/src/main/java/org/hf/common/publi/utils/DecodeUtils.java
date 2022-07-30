package org.hf.common.publi.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;

/**
 * <p> 解密工具类   实现:AES </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 12:23
 */
public class DecodeUtils {

    /**
     * AES 解密方法
     * @param encodeRules       解密秘钥
     * @param content           解密的内容
     * @return 如果返回 "" 则解密失败, 否则解密成功
     */
    public static String aesDecode(String encodeRules, String content){
        try {
            //传入参数异常, 加密失败
            if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encodeRules)) {
                return "";
            }
            //8.将加密并编码后的内容解码成字节数组
            byte [] byteContent = Base64Utils.stringToByte(content);
            if(byteContent == null) {
                return "";
            }
            //解密
            byte[] byteAes = EncryptUtils.aesPublic(encodeRules, byteContent, Cipher.DECRYPT_MODE);
            if (byteAes == null) {
                return "";
            }
            //10.将解密后的数据转换成字符串, 并返回
            return new String(byteAes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果有错就返加 ""
        return "";
    }

}
