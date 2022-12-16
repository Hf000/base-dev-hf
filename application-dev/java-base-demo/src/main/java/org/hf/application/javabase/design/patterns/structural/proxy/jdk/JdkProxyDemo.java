package org.hf.application.javabase.design.patterns.structural.proxy.jdk;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * <p> 代理模式-jdk动态代理 </p>
 *
 * @author hufei
 * @date 2022/10/6 10:52
 */
public class JdkProxyDemo {

    public static void main(String[] args) {
        try {
            //需要被创建代理的对象
            LandlordService landlordService = new LandlordServiceImpl();
            //创建代理
            LandlordService proxyLandlordService = (LandlordService) Proxy.newProxyInstance(
                    landlordService.getClass().getClassLoader(),
                    //被代理的对象实现的所有接口
                    LandlordServiceImpl.class.getInterfaces(),
                    //代理过程->InvocationHandler
                    new ProxyImpl(landlordService)
            );
            proxyLandlordService.pay("王五");
//            generateProxyClass(proxyLandlordService, "d:\\$Proxy0.class");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成指定对象的代理类并保存到指定位置
     * @param proxyObject 需要生成代理类的对象
     * @param filePath 字节码文件保存位置,磁盘路径
     */
    /*@SneakyThrows
    private static void generateProxyClass(Object proxyObject, String filePath) throws IOException {
        if (proxyObject == null || StringUtils.isBlank(filePath)) {
            return;
        }
        // 获取指定对象生成的代理类字节码数组
        byte[] proxyClass = ProxyGenerator.generateProxyClass(proxyObject.getClass().getSimpleName(), proxyObject.getClass().getInterfaces());
        //将字节码文件保存到D盘，文件名为$Proxy0.class
        FileOutputStream outputStream = new FileOutputStream(new File(filePath));
        outputStream.write(proxyClass);
        outputStream.flush();
        outputStream.close();
    }*/
}
