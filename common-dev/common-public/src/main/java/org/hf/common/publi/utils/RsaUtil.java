package org.hf.common.publi.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> rsa util </p >
 * @author HUFEI
 * @date 2023-06-06
 **/
@Slf4j
public class RsaUtil {

    private static final String FULL_CIPHER_NAME = "RSA/ECB/PKCS1Padding";
    private static final String ALGORITHM = "RSA";
    /**
     * RSA默认加密位数 1024bit<br/>
     */
    private static final int DEFAULT_KEY_SIZE = 1024;
    /**
     * RSA加密位数 2048bit<br/>
     */
    private static final int KEY_SIZE_2048 = 2048;

    private RsaUtil() {
    }

    /**
     * 公钥加密<br/>
     * param就是请求的参数<br/>
     */
    public static String publicEncrypt(String data, String publicKey) throws GeneralSecurityException, IOException {
        return doPublicEncrypt(data, getPublicKeyFromX509(publicKey), DEFAULT_KEY_SIZE);
    }

    /**
     * 公钥加密<br/>
     * param就是请求的参数<br/>
     */
    public static String publicEncrypt(final Map<String, String> params, String publicKey) throws GeneralSecurityException, IOException {
        return doPublicEncrypt(JSON.toJSONString(parseString(params)), getPublicKeyFromX509(publicKey), DEFAULT_KEY_SIZE);
    }

    /**
     * 公钥加密2048<br/>
     * param就是请求的参数<br/>
     */
    public static String publicEncrypt2048(String data, String publicKey) throws GeneralSecurityException, IOException {
        return doPublicEncrypt(data, getPublicKeyFromX509(publicKey), KEY_SIZE_2048);
    }

    /**
     * 公钥加密2048<br/>
     * param就是请求的参数<br/>
     */
    public static String publicEncrypt2048(final Map<String, String> params, String publicKey) throws GeneralSecurityException, IOException {
        return doPublicEncrypt(JSON.toJSONString(parseString(params)), getPublicKeyFromX509(publicKey), KEY_SIZE_2048);
    }

    /**
     * 私钥解密<br/>
     * data就是待解密的数据<br/>
     */
    public static String privateDecrypt(String data, String privateKey) throws GeneralSecurityException, IOException {
        String decrypt = doPrivateDecrypt(data, getPrivateKeyFromPkcs8(privateKey), DEFAULT_KEY_SIZE);
        return URLDecoder.decode(decrypt, StandardCharsets.UTF_8.name());
    }

    /**
     * 私钥解密2048<br/>
     * data就是待解密的数据<br/>
     */
    public static String privateDecrypt2048(String data, String privateKey) throws GeneralSecurityException, IOException {
        String decrypt = doPrivateDecrypt(data, getPrivateKeyFromPkcs8(privateKey), KEY_SIZE_2048);
        return URLDecoder.decode(decrypt, StandardCharsets.UTF_8.name());
    }

    /**
     * 公钥加密<br/>
     * @param data 待加密数据
     */
    private static String doPublicEncrypt(String data, Key publicKey, int keySize) throws GeneralSecurityException, IOException {
        int maxEncryptBlock = keySize / 8 - 11;
//        int maxEncryptBlock = ((RSAKey) publicKey).getModulus().bitLength() / 8 - 11;
        Cipher cipher = Cipher.getInstance(FULL_CIPHER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes(StandardCharsets.UTF_8).length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > maxEncryptBlock) {
                cache = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8), offset, maxEncryptBlock);
            } else {
                cache = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * maxEncryptBlock;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * 私钥解密<br/>
     * @param data 待解密数据
     */
    private static String doPrivateDecrypt(String data, Key privateKey, int keySize) throws GeneralSecurityException, IOException {
        int maxDecryptBlock = keySize / 8;
//        int maxDecryptBlock = ((RSAKey) privateKey).getModulus().bitLength() / 8;
        Cipher cipher = Cipher.getInstance(FULL_CIPHER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > maxDecryptBlock) {
                cache = cipher.doFinal(dataBytes, offset, maxDecryptBlock);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * maxDecryptBlock;
        }
        out.close();
        // 解密后的内容
        return out.toString(StandardCharsets.UTF_8.name());
    }

    /**
     * 获取公钥对象<br/>
     * @param publicKey 公钥字符串
     */
    public static PublicKey getPublicKeyFromX509(String publicKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes(StandardCharsets.UTF_8));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥对象<br/>
     * @param privateKey 私钥字符串
     */
    public static PrivateKey getPrivateKeyFromPkcs8(String privateKey) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    private static LinkedHashMap<String, String> parseString(final Map<String, String> requestMap) {
        List<String> keyList = new ArrayList<>(requestMap.keySet());
        LinkedHashMap<String, String> parseMap = new LinkedHashMap<>(requestMap.size());
        Collections.sort(keyList);
        for (String key : keyList) {
            String value = requestMap.get(key);
            try {
                if (value != null) {
                    value = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            parseMap.put(key, value);
        }
        return parseMap;
    }

    private static String generatePublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return Base64.encodeBase64String(bytes);
    }

    private static String generatePrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 生成默认1024bit密钥对<br/>
     */
    private static KeyPair generatorKeyPair() throws NoSuchAlgorithmException {
        return generatorKeyPair(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成指定长度密钥对<br/>
     */
    private static KeyPair generatorKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        // 设置秘钥长度
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 生成秘钥对，私钥格式：pkcs8，公钥格式：X509<br/>
     *
     * @throws NoSuchAlgorithmException 异常
     */
    public static void generateKey() throws GeneralSecurityException, IOException {
        KeyPair keyPair = generatorKeyPair();
        String privateKey = generatePrivateKey(keyPair);
        String publicKey = generatePublicKey(keyPair);
        log.info("privateKey:{}", privateKey);
        log.info("publicKey:{}", publicKey);
        String content = "RSA公开密钥密码体制是一种使用不同的加密密钥与解密密钥，“由已知加密密钥推导出解密密钥在计算上是不可行的”密码体制 [2]  。\n" +
                "在公开密钥密码体制中，加密密钥（即公开密钥）PK是公开信息，而解密密钥（即秘密密钥）SK是需要保密的。加密算法E和解密算法D也都是公开的。虽然解密密钥SK是由公开密钥PK决定的，但却不能根据PK计算出SK [2]  。\n" +
                "正是基于这种理论，1978年出现了著名的RSA算法，它通常是先生成一对RSA密钥，其中之一是保密密钥，由用户保存；另一个为公开密钥，可对外公开，甚至可在网络服务器中注册。为提高保密强度，RSA密钥至少为500位长，一般推荐使用1024位。这就使加密的计算量很大。为减少计算量，在传送信息时，常采用传统加密方法与公开密钥加密方法相结合的方式，即信息采用改进的DES或IDEA对话密钥加密，然后使用RSA密钥加密对话密钥和信息摘要。对方收到信息后，用不同的密钥解密并可核对信息摘要 [2]  。\n" +
                "RSA是被研究得最广泛的公钥算法，从提出到现在已近三十年，经历了各种攻击的考验，逐渐为人们接受，普遍认为是目前最优秀的公钥方案之一。1983年麻省理工学院在美国为RSA算法申请了专利 [3]  。\n" +
                "RSA允许你选择公钥的大小。512位的密钥被视为不安全的；768位的密钥不用担心受到除了国家安全管理（NSA）外的其他事物的危害；1024位的密钥几乎是安全的。RSA在一些主要产品内部都有嵌入，像 Windows、网景 Navigator、 Quicken和 Lotus Notes [3]  。";
        String s = publicEncrypt(content, publicKey);
        log.info(privateDecrypt(s, privateKey));
    }

    public static void generateKey2048() throws GeneralSecurityException, IOException {
        KeyPair keyPair = generatorKeyPair(KEY_SIZE_2048);
        String privateKey = generatePrivateKey(keyPair);
        String publicKey = generatePublicKey(keyPair);
        log.info("privateKey:{}", privateKey);
        log.info("publicKey:{}", publicKey);
        String content = "RSA公开密钥密码体制是一种使用不同的加密密钥与解密密钥，“由已知加密密钥推导出解密密钥在计算上是不可行的”密码体制 [2]  。\n" +
                "在公开密钥密码体制中，加密密钥（即公开密钥）PK是公开信息，而解密密钥（即秘密密钥）SK是需要保密的。加密算法E和解密算法D也都是公开的。虽然解密密钥SK是由公开密钥PK决定的，但却不能根据PK计算出SK [2]  。\n" +
                "正是基于这种理论，1978年出现了著名的RSA算法，它通常是先生成一对RSA密钥，其中之一是保密密钥，由用户保存；另一个为公开密钥，可对外公开，甚至可在网络服务器中注册。为提高保密强度，RSA密钥至少为500位长，一般推荐使用1024位。这就使加密的计算量很大。为减少计算量，在传送信息时，常采用传统加密方法与公开密钥加密方法相结合的方式，即信息采用改进的DES或IDEA对话密钥加密，然后使用RSA密钥加密对话密钥和信息摘要。对方收到信息后，用不同的密钥解密并可核对信息摘要 [2]  。\n" +
                "RSA是被研究得最广泛的公钥算法，从提出到现在已近三十年，经历了各种攻击的考验，逐渐为人们接受，普遍认为是目前最优秀的公钥方案之一。1983年麻省理工学院在美国为RSA算法申请了专利 [3]  。\n" +
                "RSA允许你选择公钥的大小。512位的密钥被视为不安全的；768位的密钥不用担心受到除了国家安全管理（NSA）外的其他事物的危害；1024位的密钥几乎是安全的。RSA在一些主要产品内部都有嵌入，像 Windows、网景 Navigator、 Quicken和 Lotus Notes [3]  。";
        String s = publicEncrypt2048(content, publicKey);
        log.info(privateDecrypt2048(s, privateKey));
    }
}