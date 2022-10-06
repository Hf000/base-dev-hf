package org.hf.application.javabase.design.patterns.creational.singleton;

import lombok.extern.slf4j.Slf4j;

/**
 * <p> 单例模式demo </p>
 * 单例模式: 某个类只能有一个实例, 提供一个全局的访问点
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/11/15 21:40
 */
@Slf4j
public class SingleDemo {

    public static void main(String[] args) {
        SingleModel singleModel1 = SingleModel.getInstance();
        SingleModel singleModel2 = SingleModel.getInstance();
        log.info("多重判加锁空单例测试===>{}", singleModel1 == singleModel2);

        SingleStaticModel singleStaticModel1 = SingleStaticModel.getInstance();
        SingleStaticModel singleStaticModel2 = SingleStaticModel.getInstance();
        log.info("静态内部类单例测试===>{}", singleStaticModel1 == singleStaticModel2);

        SingleEnumModel singleEnumModel1 = SingleEnumModel.getInstance();
        SingleEnumModel singleEnumModel2 = SingleEnumModel.getInstance();
        log.info("内部枚举类单例测试===>{}", singleEnumModel1 == singleEnumModel2);
    }
}
