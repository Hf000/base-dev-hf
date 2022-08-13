package org.hf.boot.springboot.controller;

import org.hf.boot.springboot.task.Result;
import org.hf.boot.springboot.task.dynamicTask.ElasticJobDynamicTask;
import org.hf.boot.springboot.task.dynamicTask.ElasticJobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * <p> elastic-job测试类 </p>
 * //@ApiIgnore  // swagger忽略扫描注解, 添加此注解的类接口不会生成文档
 * @author hufei
 * @date 2022/8/13 8:59
*/
@ApiIgnore
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    @Autowired
    ElasticJobHandler elasticJobHandler;

    /***
     * 动态创建任务
     * @param times:延迟时间，为了测试到效果，所以在当前时间往后延迟
     * @param jobname:任务名字
     * @param param:自定义参数
     * @return 返回结果
     */
    @GetMapping
    public Result<Void> add(Long times, String jobname, String param){
        //在当前指定时间内延迟times毫秒执行任务
        Date date = new Date(System.currentTimeMillis()+times);
        //需要传递给定时任务的参数
        String cron = ElasticJobHandler.getCron(date);
        //执行任务
        elasticJobHandler.addPublishJob(cron,param,jobname,new ElasticJobDynamicTask());
        return new Result<>(true, 200,"添加任务成功！");
    }
}
