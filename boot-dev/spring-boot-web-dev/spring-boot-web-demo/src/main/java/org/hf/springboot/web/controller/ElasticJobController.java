package org.hf.springboot.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.web.pojo.vo.ResponseVO;
import org.hf.common.web.utils.ResponseUtil;
import org.hf.springboot.service.config.ElasticJobHandler;
import org.hf.springboot.service.pojo.bo.ElasticJobBO;
import org.hf.springboot.service.service.ElasticJobService;
import org.hf.springboot.service.task.ElasticJobDynamicTask;
import org.hf.springboot.web.convert.ElasticJobBeansConvert;
import org.hf.springboot.web.pojo.vo.ElasticJobVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @Autowired
    private ElasticJobHandler elasticJobHandler;

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

    /***
     * 动态创建任务
     * @param cron:cron表达式
     * @param param:自定义参数
     * @return 返回结果
     */
    @GetMapping("addJob")
    public ResponseVO<Void> addJob(@RequestParam("cron") String cron, @RequestParam("param") String param){
        //需要传递给定时任务的参数
        if (StringUtils.isBlank(cron)) {
            //在当前指定时间内延迟times毫秒执行任务,  如果没有cron则指定3秒后执行一次
            Date date = new Date(System.currentTimeMillis() + 3000);
            cron = ElasticJobHandler.getCron(date);
        }
        //执行任务
        elasticJobHandler.addPublishJob(cron, param, new ElasticJobDynamicTask());
        return ResponseUtil.success();
    }

}
