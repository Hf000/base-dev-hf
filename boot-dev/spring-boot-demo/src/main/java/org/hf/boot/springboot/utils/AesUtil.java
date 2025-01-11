package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.hf.boot.springboot.error.BusinessException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * <p> AES加密工具类 </p >
 * 对称加密
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

    public static String encryptDefault(String content) {
        return encryptBase64(content, DEFAULT_KEY256);
    }

    public static String decryptDefault(String encryptedString) {
        return decryptBase64(encryptedString, DEFAULT_KEY256);
    }

    /**
     * 根据key加密数据
     * @param content 需要加密的数据
     * @param key     base64后的key
     * @return 加密后base64数据
     */
    public static String encryptBase64(String content, String key) {
        byte[] keyBytes = Base64.decodeBase64(key);
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = doDecryptOrEncrypt(CIPHER_ALGORITHM, Cipher.ENCRYPT_MODE, contentBytes,
                new SecretKeySpec(keyBytes, KEY_ALGORITHM), null);
        if (bytes == null) {
            throw new BusinessException("加密失败");
        }
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 根据key 解密
     * @param encryptedString 加密后base64数据
     * @param keyBase64       base64后的key
     * @return 解密后数据
     */
    public static String decryptBase64(String encryptedString, String keyBase64) {
        byte[] encryptedBytes = Base64.decodeBase64(encryptedString);
        byte[] keyBytes = Base64.decodeBase64(keyBase64);
        byte[] bytes = doDecryptOrEncrypt(CIPHER_ALGORITHM, Cipher.DECRYPT_MODE, encryptedBytes,
                new SecretKeySpec(keyBytes, KEY_ALGORITHM), null);
        if (bytes == null) {
            throw new BusinessException("解密失败");
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("all")
    private static byte[] doDecryptOrEncrypt(String algorithm, int cipherMode, byte[] encryptedBytes,
                                             SecretKeySpec key, AlgorithmParameterSpec spec) {
        try {
            // create AES Cipher
            Cipher cipher = Cipher.getInstance(algorithm);
            if (spec != null) {
                cipher.init(cipherMode, key, spec);
            } else {
                cipher.init(cipherMode, key);
            }
            // 解密/加密
            return cipher.doFinal(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }
    /***************************************方式一结束********************************************/
}