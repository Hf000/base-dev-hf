package org.hf.common.publi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.constants.CommonConstant;
import org.hf.common.publi.constants.NumberConstant;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * <p> 加密工具类   实现:AES </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 12:23
 */
@Slf4j
public class EncryptUtils {

    /**
     * AES加密方法
     * @param encodeRules   加密秘钥
     * @param content       需要加密的内容
     * @return 如果返回 "" 则加密失败, 否则加密成功
     */
    public static String aesEncrypt(String encodeRules, String content){
        try {
            //传入参数异常, 加密失败
            if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encodeRules)) {
                return "";
            }
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte [] byteContent = content.getBytes(StandardCharsets.UTF_8);
            //加密
            byte[] byteAes = aesPublic(encodeRules, byteContent, Cipher.ENCRYPT_MODE);
            if (byteAes == null) {
                return "";
            }
            //10.将加密后的数据转换为字符串, 并返回加密成功后的字符串
            return Base64Utils.byteToString(byteAes);
        } catch (Exception e) {
            log.error("加密失败!!!", e);
        }
        //如果有错就返回一个空字符串
        return "";
    }

    /**
     * AES 加密/解密公用方法
     * @param encodeRules       加密/解密 秘钥
     * @param byteContent       加密/解密 byte[] 内容
     * @param cipherInt         加密/解密 类型
     * @return  返回类型 byte[]
     * @throws Exception 异常
     */
    public static byte[] aesPublic(String encodeRules, byte[] byteContent, int cipherInt) throws Exception {
        //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen = KeyGenerator.getInstance(CommonConstant.AES);
        //2.根据ecnodeRules规则初始化密钥生成器    生成一个128位的随机源,根据传入的字节数组
        keygen.init(NumberConstant.INT_128, new SecureRandom(encodeRules.getBytes()));
        //3.产生原始对称密钥
        SecretKey originalKey = keygen.generateKey();
        //4.获得原始对称密钥的字节数组
        byte [] raw = originalKey.getEncoded();
        //5.根据字节数组生成AES密钥
        SecretKey key = new SecretKeySpec(raw, CommonConstant.AES);
        //6.根据指定算法AES生成密码器
        Cipher cipher = Cipher.getInstance(CommonConstant.AES);
        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(cipherInt, key);
        //9.根据密码器的初始化方式--加密/解密：将数据加密/解密   并返回
        return cipher.doFinal(byteContent);
    }

}
