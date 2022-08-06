package org.hf.springboot.service.pojo.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hf.common.config.event.EventBaseAbstract;

/**
 * <p> 事件实体 </p>
 * 如果不需要默认逻辑, 可以直接实现EventBase接口
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 17:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EventBO extends EventBaseAbstract {

    private Integer id;

    private String action;

    private String eventName;

}
