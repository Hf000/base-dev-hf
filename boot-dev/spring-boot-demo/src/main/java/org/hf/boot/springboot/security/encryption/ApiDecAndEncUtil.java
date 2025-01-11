package org.hf.boot.springboot.security.encryption;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.utils.AesUtil;
import org.hf.boot.springboot.utils.RsaUtil;

/**
 * 入参解密,出参加密工具
 * @author HF
 */
@Slf4j
public class ApiDecAndEncUtil {

    /**
     * 入参解密
     * @param req 请求数据
     * @return 返回解密后的数据
     */
    public static String getDecryptData(ApiDecAndEncReq req, String inPriKey) {
        if (req == null || StringUtils.isBlank(inPriKey)) {
            throw new BusinessException("入参解密参数异常");
        }
        String decryptData;
        try {
            // 通过RSA非对称方式解密获得AES解密秘钥
            String reqAesKey = RsaUtil.privateDecrypt(req.getEncryptedKey(), inPriKey);
            // 通过AES对称方式解密获得业务数据
            decryptData = AesUtil.decryptBase64(req.getEncryptedData(), reqAesKey);
        } catch (Exception e) {
            throw new BusinessException("入参解密失败", e);
        }
        return decryptData;
    }

    /**
     * 出参加密
     * @param respStr 返回数据明文
     * @return 返回加密后的数据
     */
    public static ApiDecAndEncResp buildEncryptedData(String respStr, String outPublicKey) {
        ApiDecAndEncResp result = new ApiDecAndEncResp();
        if (StringUtils.isBlank(respStr)) {
            return result;
        }
        try {
            // 生成一个对称方式AES加密的秘钥key
            String respAesKey = AesUtil.genKey();
            // AES对称方式加密响应数据
            result.setEncryptedData(AesUtil.encryptBase64(respStr, respAesKey));
            // RSA非对称方式加密AES的秘钥
            result.setEncryptedKey(RsaUtil.publicEncrypt(respAesKey, outPublicKey));
        } catch (Exception e) {
            throw new BusinessException("响应结果加密错误", e);
        }
        return result;
    }
}