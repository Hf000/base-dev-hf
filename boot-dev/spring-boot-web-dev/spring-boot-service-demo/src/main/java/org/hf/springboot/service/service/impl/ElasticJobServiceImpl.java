package org.hf.springboot.service.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.config.LiteJobConfigurationGsonFactory;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.config.elasticjob.ElasticJobAutoConfiguration;
import org.hf.common.config.elasticjob.ElasticJobProperties;
import org.hf.common.config.enums.ConfigExceptionEnum;
import org.hf.springboot.service.constants.ServiceConstant;
import org.hf.springboot.service.enums.ExceptionEnum;
import org.hf.springboot.service.exception.BusinessException;
import org.hf.springboot.service.pojo.bo.ElasticJobBO;
import org.hf.springboot.service.service.ElasticJobService;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <p> 定时任务支持接口实现 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 9:16
 */
@Slf4j
@Service
public class ElasticJobServiceImpl implements ElasticJobService {

    private static final Map<String, ElasticJobBO> JOB_INFO = Maps.newConcurrentMap();

//    private final ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Autowired(required = false)
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    /**
     * 获取zookeeper的注册中心连接
     * @param elasticJobProperties zk连接配置项
     */
    /*public ElasticJobServiceImpl(ElasticJobProperties elasticJobProperties) {
        // 初始化zookeeper配置
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(elasticJobProperties.getAddress(), elasticJobProperties.getNamespace());
        if (elasticJobProperties.isFlag()) {
            zookeeperConfiguration.setBaseSleepTimeMilliseconds(elasticJobProperties.getBaseSleepTimeMilliseconds());
            zookeeperConfiguration.setMaxSleepTimeMilliseconds(elasticJobProperties.getMaxSleepTimeMilliseconds());
            zookeeperConfiguration.setMaxRetries(elasticJobProperties.getMaxRetries());
        }
        // 初始注册中心,建立连接
        zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        zookeeperRegistryCenter.init();
    }*/

    /**
     * 初始化获取注册中心的所有定时任务
     */
    @PostConstruct
    public void init() {
        JOB_INFO.clear();
        if (zookeeperRegistryCenter == null) {
            log.error("定时任务初始化失败, 定时任务配置初始化开关关闭, 请检查配置ej.open参数");
            return;
        }
        // 获取根目录下的所有定时任务名称
        List<String> jobNames = zookeeperRegistryCenter.getChildrenKeys("/");
        jobNames.forEach(jobName -> {
            LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(jobName);
            Optional.ofNullable(liteJobConfiguration).ifPresent(jobConfig -> {
                ElasticJobBO elasticJobBO = new ElasticJobBO();
                elasticJobBO.setJobName(jobName);
                elasticJobBO.setCron(liteJobConfiguration.getTypeConfig().getCoreConfig().getCron());
                JOB_INFO.put(jobName, elasticJobBO);
            });
        });
    }

    /**
     * 获取指定任务的具体信息
     * @param jobName 任务名称
     * @return 返回任务信息
     */
    private LiteJobConfiguration getLiteJobConfiguration(String jobName) {
        if (StringUtils.isBlank(jobName)) {
            return null;
        }
        JobNodePath jobNodePath = new JobNodePath(jobName);
        if (zookeeperRegistryCenter == null) {
            throw new BusinessException(ConfigExceptionEnum.JOB_CONFIG_ERROR);
        }
        String liteJobConfigJson = zookeeperRegistryCenter.get(jobNodePath.getConfigNodePath());
        if (Objects.isNull(liteJobConfigJson)) {
            return null;
        }
        return LiteJobConfigurationGsonFactory.fromJson(liteJobConfigJson);
    }

    @Override
    public List<ElasticJobBO> findElasticJobList() {
        return Lists.newArrayList(JOB_INFO.values());
    }

    @Override
    public void executeTask(String jobName) {
        ElasticJobBO elasticJobBO = JOB_INFO.get(jobName);
        if (Objects.isNull(elasticJobBO)) {
            throw new BusinessException(ExceptionEnum.EXECUTE_JOB_ERROR);
        }
        if (zookeeperRegistryCenter == null) {
            throw new BusinessException(ConfigExceptionEnum.JOB_CONFIG_ERROR);
        }
        JobNodePath jobNodePath = new JobNodePath(jobName);
        for (String childrenKey : zookeeperRegistryCenter.getChildrenKeys(jobNodePath.getInstancesNodePath())) {
            zookeeperRegistryCenter.persist(jobNodePath.getInstanceNodePath(childrenKey), ServiceConstant.JOB_TRIGGER);
        }
    }

    @Override
    public void rescheduleJob(String jobName, String cron) {
        ElasticJobBO elasticJobBO = JOB_INFO.get(jobName);
        if (Objects.isNull(elasticJobBO)) {
            throw new BusinessException(ExceptionEnum.EXECUTE_JOB_ERROR);
        }
        if (!checkCronExpression(cron)) {
            throw new BusinessException(ExceptionEnum.JOB_CRON_ERROR);
        }
        if (zookeeperRegistryCenter == null) {
            throw new BusinessException(ConfigExceptionEnum.JOB_CONFIG_ERROR);
        }
        LiteJobConfiguration liteJobConfiguration = getLiteJobConfiguration(jobName);
        Optional.ofNullable(liteJobConfiguration).ifPresent(jobConfig -> {
            ReflectUtil.setFieldValue(jobConfig.getTypeConfig().getCoreConfig(), ServiceConstant.CRON, cron);
            JobNodePath jobNodePath = new JobNodePath(jobName);
            zookeeperRegistryCenter.update(jobNodePath.getConfigNodePath(), LiteJobConfigurationGsonFactory.toJsonForObject(jobConfig));
            elasticJobBO.setCron(cron);
        });
    }

    /**
     * 校验cron表达式是否正确
     * @param param cron表达式
     * @return 返回
     */
    protected boolean checkCronExpression(String param) {
        if (StringUtils.isBlank(param)) {
            return false;
        }
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        try {
            cronTrigger.setCronExpression(param);
            Date date = cronTrigger.computeFirstFireTime(null);
            return date != null && date.after(new Date());
        } catch (ParseException e) {
            log.error("cron表达式格式错误", e);
        }
        return false;
    }

}
