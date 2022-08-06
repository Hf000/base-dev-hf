package org.hf.springboot.service.pojo.bo;

import lombok.Data;

/**
 * <p> 定时任务业务BO </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 9:48
 */
@Data
public class ElasticJobBO {

    /**
     * 定时任务名称
     */
    private String jobName;

    /**
     * cron表达式
     */
    private String cron;

}
