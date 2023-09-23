package org.hf.common.web.utils;

import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.constants.CommonConstant;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * <p> Ip相关工具类 </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
public class IpUtil {

    private final static String LOCALHOST = "0:0:0:0:0:0:0:1";
    private final static String LOCALHOST2 = "127.0.0.1";
    private final static String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
    private final static String UNKNOWN = "unknown";
    private final static String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private final static String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    /**
     * 获取请求ip
     * @param request 请求
     * @return 请求ip
     */
    public static String getReqIp(HttpServletRequest request) {
        String ip = request.getHeader(HEADER_X_FORWARDED_FOR);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照","分割
        if (ip != null && StringUtils.isNotBlank(ip.trim()) && !CommonConstant.NULL_STRING.equalsIgnoreCase(ip.trim()) && ip.contains(CommonConstant.EN_COMMA)) {
            ip = ip.split(CommonConstant.EN_COMMA)[0];
        }
        // 判断是否是本地机器地址
        if (LOCALHOST2.equals(ip) || LOCALHOST.equals(ip)){
            InetAddress inet = null;
            try {
                inet = InetAddress.getLocalHost();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (inet != null) {
                ip = inet.getHostAddress();
            }
        }
        return ip;
    }

}