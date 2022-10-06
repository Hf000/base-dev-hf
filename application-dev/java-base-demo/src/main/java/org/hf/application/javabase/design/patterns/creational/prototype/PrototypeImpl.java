package org.hf.application.javabase.design.patterns.creational.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * 原型实现
 * @author HF
 */
public class PrototypeImpl implements IPrototype {

    private final List<String> parts;

    public PrototypeImpl() {
        this.parts = new ArrayList<>();
    }

    public PrototypeImpl(List<String> parts) {
        this.parts = parts;
    }

    /**
     * 耗时的数据加载操作
     */
    public void loadData() {
        // 这里是为了防止上一次操作而出现重复内容
        parts.clear();
        // 这里是一些固定的内容, 多次操作都是重复的内容就可以使用原型模式
        parts.add("老夫聊发少年狂，左牵黄，右擎苍，锦帽貂裘，千骑卷平冈。");
        parts.add("为报倾城随太守，亲射虎，看孙郎。");
        parts.add("酒酣胸胆尚开张，鬓微霜，又何妨！持节云中，何日遣冯唐？");
        parts.add("会挽雕弓如满月，西北望，射天狼。");
    }

    /**
     * 获取固定的原型内容
     * @return 固定原型内容
     */
    public List<String> getContents() {
        return parts;
    }

    @Override
    public IPrototype copy() {
        List<String> cloneList = new ArrayList<>(parts);
        return new PrototypeImpl(cloneList);
    }
}