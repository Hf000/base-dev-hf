package org.hf.application.javabase.design.patterns.structural.proxy.jdk;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;


public class JdkProxyTest {

    public static void main(String[] args) {
        try {
            //需要被创建代理的对象
            LandlordService landlordService = new Landlord();

            //创建代理
            LandlordService proxyLandlordService = (LandlordService) Proxy.newProxyInstance(
                    landlordService.getClass().getClassLoader(),
                    Landlord.class.getInterfaces(), //被代理的对象实现的所有接口
                    //代理过程->InvocationHandler
                    new QFang(landlordService)
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
