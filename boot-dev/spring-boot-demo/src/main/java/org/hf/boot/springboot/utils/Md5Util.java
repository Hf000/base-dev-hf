package org.hf.boot.springboot.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p> MD5工具类 </p >
 * @author hufei
 * @date 2023-04-11
 **/
public final class Md5Util {

    private static final String MD5 = "MD5";

    private static final String SHA256 = "SHA-256";

    private static final String HEX_CHARS = "0123456789abcdef";

    /**
     * MD5加码 生成32位md5码
     * @param inStr 入参
     * @return 加密后的字符串
     */
    public static String stringToMd5(String inStr) {
        if (inStr == null) {
            return null;
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(MD5);
        } catch (Exception e) {
            return inStr;
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append('0');
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     * @param inStr 入参
     * @return 加密后的字符串
     */
    public static String convertMd5(String inStr) {
        char[] chars = inStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] ^ 't');
        }
        return new String(chars);
    }

    /**
     * 将流加密
     * @param inputStream 输入流
     * @return 加密后的字符串
     * @throws IOException 异常
     */
    public static String fileToMd5(InputStream inputStream) throws IOException {
        String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(inputStream));
        IOUtils.closeQuietly(inputStream);
        return md5;
    }

    /**
     * 获取MessageDigest MD5
     * @return MessageDigest
     */
    private static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取MessageDigest SHA-256
     * @return MessageDigest
     */
    private static MessageDigest getDigestBySha() {
        try {
            return MessageDigest.getInstance(SHA256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * MD5加密，并返回作为一个十六进制字节
     * @param data 字节数组
     * @return 加密后的字节数组
     */
    public static byte[] md5(byte[] data) {
        return getDigest().digest(data);
    }

    /**
     * SHA-256加密，并返回作为一个十六进制字节
     * @param data 字节数组
     * @return 加密后的字节数组
     */
    public static byte[] sha256(byte[] data) {
        return getDigestBySha().digest(data);
    }

    /**
     * MD5加密，并返回作为一个十六进制字节
     * @param data 字符串
     * @return 加密后的字符串
     */
    public static byte[] md5(String data) {
        return md5(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * MD5加密后hash
     * @param data 字节数组
     * @return 加密后的字符串
     */
    public static String md5Hex(byte[] data) {
        return toHexString(md5(data));
    }

    /**
     * MD5加密后hash
     * @param data 字符串
     * @return 加密后的字符串
     */
    public static String md5Hex(String data) {
        return toHexString(md5(data));
    }

    /**
     * SHA256加密后hash
     * @param data 字符串
     * @return 加密后的字符串
     */
    public static String sha256Hex(String data) {
        return toHexString(sha256(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 按照指定的规则进行hash
     * @param bytes 字节数组
     * @return hash后字符串
     */
    private static String toHexString(byte[] bytes) {
        StringBuilder stringbuilder = new StringBuilder();
        for (byte aByte : bytes) {
            stringbuilder.append(HEX_CHARS.charAt(aByte >>> 4 & 0x0F));
            stringbuilder.append(HEX_CHARS.charAt(aByte & 0x0F));
        }
        return stringbuilder.toString();
    }

    /**
     * 二进制byte数组转十六进制
     * @param bin byte array
     * @return hex string
     */
    public static String byteToHex(byte[] bin) {
        return Hex.encodeHexString(bin);
    }

    /**
     * 十六进制转二进制byte数组
     * @param hex hex string
     * @return byte array
     */
    public static byte[] hexToByte(String hex) {
        try {
            return Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }
}