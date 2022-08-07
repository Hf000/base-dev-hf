package org.hf.application.javabase.design.patterns.creational.singleton;

/**
 * <p> 单例模式-内部枚举 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 13:28
 */
public class SingleEnumModel {

    private SingleEnumModel(){}

    /**
     * 获取单例对象
     * @return 单例对象实例
     */
    public static SingleEnumModel getInstance() {
        return SingleEnum.SINGLE_INSTANCE.getInstance();
    }

    /**
     * 枚举
     */
    enum SingleEnum{
        /**
         * 枚举单例
         */
        SINGLE_INSTANCE;
        private final SingleEnumModel singleEnumModel;
        SingleEnum() {
            singleEnumModel = new SingleEnumModel();
        }
        public SingleEnumModel getInstance() {
            return SINGLE_INSTANCE.singleEnumModel;
        }
    }
}
