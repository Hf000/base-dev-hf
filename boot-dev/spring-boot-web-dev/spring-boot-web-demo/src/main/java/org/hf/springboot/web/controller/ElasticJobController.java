package org.hf.springboot.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.web.pojo.vo.ResponseVO;
import org.hf.common.web.utils.ResponseUtil;
import org.hf.springboot.service.pojo.bo.ElasticJobBO;
import org.hf.springboot.service.service.ElasticJobService;
import org.hf.springboot.web.convert.ElasticJobBeansConvert;
import org.hf.springboot.web.pojo.vo.ElasticJobVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p> 定时任务获取支持Controller </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 9:12
 */
@Slf4j
@RestController
@RequestMapping("support/job")
public class ElasticJobController {

    @Autowired
    private ElasticJobService elasticJobService;

    @Autowired
    private ElasticJobBeansConvert elasticJobBeansConvert;

    /**
     * 获取定时任务列表
     * @return 返回
     */
    @GetMapping("list")
    public ResponseVO<List<ElasticJobVO>> jobList() {
        List<ElasticJobBO> elasticJobBos = elasticJobService.findElasticJobList();
        List<ElasticJobVO> elasticJobVos = elasticJobBeansConvert.elasticJobBoToVo(elasticJobBos);
        return ResponseUtil.success(elasticJobVos);
    }

    /**
     * 触发指定的定时任务
     * @param jobName 任务名称
     * @return 返回
     */
    @GetMapping("execute/{jobName}")
    public ResponseVO<Void> executeTask(@PathVariable("jobName") String jobName) {
        elasticJobService.executeTask(jobName);
        return ResponseUtil.success();
    }

    /**
     * 重新调度定时任务
     * @param jobName 定时任务名称
     * @param cron cron表达式
     * @return 返回
     */
    @GetMapping("rescheduleJob/{jobName}")
    public ResponseVO<Void> rescheduleJob(@PathVariable("jobName") String jobName, @RequestParam("cron") String cron) {
        elasticJobService.rescheduleJob(jobName, cron);
        return ResponseUtil.success();
    }

}
