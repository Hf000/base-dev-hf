package org.hf.common.publi.utils;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.publi.exception.BusinessException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * <p> 获取本地ip </p >
 * @author HF
 * @date 2022-11-29
 **/
@Slf4j
public class LocalIpUtils {

    /**
     * 获取本地IP地址
     * @return String
     * @throws Exception 异常
     */
    public static String getLocalIp() throws Exception{
        if(isWindowsOs()){
            return InetAddress.getLocalHost().getHostAddress();
        }else{
            return getLinuxLocalIp();
        }
    }

    /**
     * 获取linux下的IP地址
     * @return String
     */
    private static String getLinuxLocalIp() {
        String ip = "";
        try {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface ni = en.nextElement();
                String name = ni.getName();
                if(!name.contains("docker") && !name.contains("lo")){
                    for(Enumeration<InetAddress> enumIpAddr = ni.getInetAddresses(); enumIpAddr.hasMoreElements();){
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if(!inetAddress.isLoopbackAddress()){
                            String ipAddress = inetAddress.getHostAddress();
                            if(!ipAddress.contains("::") && !ipAddress.contains("0:0:") && !ipAddress.contains("fe80")){
                                ip = ipAddress;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("获取linux下的ip出错，cause：", e);
            throw new BusinessException("获取linux下的ip出错，请检查");
        }
        return ip;
    }

    /**
     * 判断是否windows操作系统
     * @return boolean true-是Windows操作系统
     */
    public static boolean isWindowsOs(){
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}