package org.hf.boot.springboot.security.encryption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 接口入参解密,出参加密秘钥配置
 * @author HF
 */
@Data
@Component
@ConfigurationProperties
public class ApiReqDecRespEncProperties {

    /**
     * 这里会将properties文件中以"api"开始的配置项都加载进来, key就是"api"后拼接的部分
     * 例如: api.hf.in.pubKey="abc", 那么map的key就是 hf
     *      api.hf.in.priKey="def", 那么map的key就是 hf
     *      api.hf.out.pubKey="ghi", 那么map的key就是 hf
     *      api.hf.out.priKey="jkl", 那么map的key就是 hf
     * 最终这里map里面就有一个元素 key=hf, 值就是填充好后的ItemProperties对象
     */
    private Map<String, ItemProperties> api;

    /**
     * 这里表示的是配置项"in"或者"out"后的内容
     */
    @Data
    public static class ItemProperties {

        private ValueProperties in;

        private ValueProperties out;

    }

    /**
     * 这里表示的就是配置项"privateKey"和"publicKey"后拼接的值
     */
    @Data
    public static class ValueProperties {

        private String priKey;

        private String pubKey;

    }

    /**
     * 获取内部私钥
     * @param appId 请求应用id
     * @return  私钥
     */
    public String getInPriKey(String appId) {
        String prefix = AppIdEnum.getPrefix(appId);
        if (StringUtils.isBlank(prefix)) {
            throw new BusinessException("接口出入参加解密,内部密钥配置有误");
        }
        if (api.get(prefix) == null || api.get(prefix).getIn() == null
                || StringUtils.isBlank(api.get(prefix).getIn().getPriKey())) {
            throw new BusinessException("接口出入参加解密,内部密钥配置有误");
        }
        return api.get(prefix).getIn().getPriKey();
    }

    public String getOutPubKey(String appId) {
        String prefix = AppIdEnum.getPrefix(appId);
        if (StringUtils.isBlank(prefix)) {
            throw new BusinessException("接口出入参加解密,外部密钥配置有误");
        }
        if (api.get(prefix) == null || api.get(prefix).getOut() == null
                || StringUtils.isBlank(api.get(prefix).getOut().getPubKey())) {
            throw new BusinessException("接口出入参加解密,外部密钥配置有误");
        }
        return api.get(prefix).getOut().getPubKey();
    }

    @AllArgsConstructor
    @Getter
    public enum AppIdEnum {

        /**
         * 枚举项
         */
        HF("HF5245245240724", "HF应用", "hf"),
        ;
        private final String appId;
        private final String appName;
        /**
         * 配置文件里的前缀,例如api.hf.in.publicKey="abc"中的hf, 也就是api这个Map中的key
         */
        private final String prefix;

        public static String getPrefix(String appId) {
            for (AppIdEnum appIdEnum : values()) {
                if (appIdEnum.getAppId().equals(appId)) {
                    return appIdEnum.getPrefix();
                }
            }
            throw new BusinessException("请求不合法");
        }
    }
}