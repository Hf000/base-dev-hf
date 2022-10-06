package org.hf.application.javabase.design.patterns.creational.singleton;

/**
 * <p> 单例模式-静态内部类方式 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 13:15
 */
public class SingleStaticModel {

    /**
     * 私有化构造
     */
    private SingleStaticModel() {
    }

    /**
     * 获取单例对象实例
     *
     * @return 单例对象
     */
    public static SingleStaticModel getInstance() {
        return InnerClassSingle.SINGLE_INSTANCE;
    }

    /**
     * 私有化内部类, 不允许外部访问, 类被加载的时候创建, 线程安全
     */
    private static class InnerClassSingle {
        private final static SingleStaticModel SINGLE_INSTANCE = new SingleStaticModel();
    }

}
