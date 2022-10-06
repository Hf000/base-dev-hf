package org.hf.application.javabase.design.patterns.structural.adapter;

import java.util.Objects;

/**
 * <p> 适配实现 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/10/6 11:19
 */
public class AdapterImpl implements OriginalInterface {

    private final FunctionInterface functionInterface;

    public AdapterImpl(FunctionInterface functionInterface) {
        this.functionInterface = functionInterface;
    }

    @Override
    public void method(String param, Integer tag) {
        Objects.requireNonNull(functionInterface);
        functionInterface.method(param);
    }
}
