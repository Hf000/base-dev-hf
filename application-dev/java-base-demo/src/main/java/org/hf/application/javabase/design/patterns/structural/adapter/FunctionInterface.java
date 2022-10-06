package org.hf.application.javabase.design.patterns.structural.adapter;

/**
 * <p> 功能接口 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/10/6 11:19
 */
public interface FunctionInterface {

    /**
     * 功能方法
     * @param param 入参
     * @param objects 入参
     */
    void method(String param, Object...objects);
}
