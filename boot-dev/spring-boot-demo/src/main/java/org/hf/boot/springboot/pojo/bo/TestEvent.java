package org.hf.boot.springboot.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hf.boot.springboot.config.AbstractEvent;

/**
 * <p> 事件测试 </p>
 * 自定义扩展Sprig事件发布订阅实现 - 4
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/25 16:11
 */
@ToString
@Setter
@Getter
public class TestEvent extends AbstractEvent {

    private String name;

}
