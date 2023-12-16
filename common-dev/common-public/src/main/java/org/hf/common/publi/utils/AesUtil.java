package org.hf.common.publi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.constants.CommonConstant;
import org.hf.common.publi.constants.NumberConstant;
import org.hf.common.publi.exception.BusinessException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <p> AES加密工具类 </p >
 * @author HUFEI
 * @date 2023-06-06
 **/
@Slf4j
public class AesUtil {

    /***************************************方式一开始********************************************/
    private static final String RANDOM_SEED = "pvePsdvhy5f4Oaw4ERertC7hMNfg2VHuObK7PYquaWU";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String DEFAULT_KEY256 = "J/Qfj4F/XZxLGg8O0kqjO3s/oPSUcqhiK+rP0RP5TVc=";
    private static final String RANDOM_TYPE = "SHA1PRNG";
    /**
     * 默认密钥大小
     */
    private static final int DEFAULT_KEY_SIZE = 128;
    /**
     * 256位密钥大小
     */
    private static final int KEY_SIZE_256 = 256;

    /**
     * 生成秘钥-长度128 base64
     * @return String
     * @throws NoSuchAlgorithmException 异常
     */
    public static String genKey() throws NoSuchAlgorithmException {
        return genKey(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成秘钥-长度256 base64
     * @return String
     * @throws NoSuchAlgorithmException 异常
     */
    public static String genKey256() throws NoSuchAlgorithmException {
        return genKey(KEY_SIZE_256);
    }

    private static String genKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = new SecureRandom();
        keyGen.init(keySize, random);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyGen.generateKey().getEncoded(), KEY_ALGORITHM);
        byte[] keyBytes = secretKeySpec.getEncoded();
        return Base64.encodeBase64String(keyBytes);
    }

    public static String encryptDefault(String content) throws IllegalBlockSizeException, BadPaddingException {
        return encryptBase64(content, DEFAULT_KEY256);
    }

    public static String decryptDefault(String encryptedString) throws IllegalBlockSizeException, BadPaddingException {
        return decryptBase64(encryptedString, DEFAULT_KEY256);
    }

    /**
     * 根据key加密数据
     * @param content 需要加密的数据
     * @param key     base64后的key
     * @return 加密后base64数据
     * @throws IllegalBlockSizeException exception
     * @throws BadPaddingException       exception
     */
    public static String encryptBase64(String content, String key) throws IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Base64.decodeBase64(key);
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        Cipher aesCipher = getAesCipher(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM));
        return Base64.encodeBase64String(aesCipher.doFinal(contentBytes));
    }

    /**
     * 根据key 解密
     * @param encryptedString 加密后base64数据
     * @param keyBase64       base64后的key
     * @return 解密后数据
     * @throws IllegalBlockSizeException exception
     * @throws BadPaddingException       exception
     */
    public static String decryptBase64(String encryptedString, String keyBase64) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = Base64.decodeBase64(encryptedString);
        byte[] keyBytes = Base64.decodeBase64(keyBase64);
        Cipher aesCipher = getAesCipher(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM));
        return new String(aesCipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
    }

    /**
     * create AES Cipher
     */
    public static Cipher getAesCipher(int cipherMode, SecretKeySpec key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 初始化
            cipher.init(cipherMode, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return cipher;
    }
    /***************************************方式一结束********************************************/
    /***************************************方式二开始********************************************/
    /**
     * AES加密
     * @param content         待加密原文
     * @param secret          base64处理后的对称秘钥
     * @param cipherTransform 秘钥全称，AES/CBC/PKCS5Padding
     * @param ivParameter     向量值  mIszfkB0kwKla6pR
     * @return 返回十六进制的加密串
     */
    public static String encryptVector(String content, String secret, String cipherTransform, String ivParameter) {
        try {
            Cipher cipher = Cipher.getInstance(cipherTransform);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(secret), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] result = cipher.doFinal(byteContent);
            return Md5Util.byteToHex(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密
     * @param content         十六进制加密串
     * @param secret          base64处理后的对称秘钥
     * @param cipherTransform 秘钥全称，AES/CBC/PKCS5Padding
     * @param ivParameter     向量值  mIszfkB0kwKla6pR
     * @param charset         加解密编码 UTF_8
     * @return 解密后的原文
     */
    public static String decryptVector(String content, String secret, String cipherTransform, String ivParameter, Charset charset) {
        try {
            Cipher cipher = Cipher.getInstance(cipherTransform);
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(secret), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter.getBytes(charset));
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] result = cipher.doFinal(Md5Util.hexToByte(content));
            return new String(result, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /***************************************方式二结束********************************************/
    /***************************************方式三开始********************************************/
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
            byte[] byteAes = aesPublic(encodeRules, byteContent, Cipher.DECRYPT_MODE);
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
    /***************************************方式三结束********************************************/
}