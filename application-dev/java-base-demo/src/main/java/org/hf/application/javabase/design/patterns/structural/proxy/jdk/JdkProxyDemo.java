package org.hf.application.javabase.design.patterns.structural.proxy.jdk;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
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
            byte[] proxyClass = ProxyGenerator.generateProxyClass(proxyLandlordService.getClass().getSimpleName(), proxyLandlordService.getClass().getInterfaces());
            //将字节码文件保存到D盘，文件名为$Proxy0.class
            FileOutputStream outputStream = new FileOutputStream(new File("d:\\$Proxy0.class"));
            outputStream.write(proxyClass);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
