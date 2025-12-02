package com.dkd.quartz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * 定时任务配置（单机部署建议删除此类和qrtz数据库表，默认走内存会最高效）集群模式调度配置
 *
 * @author ruoyi
 */
@Configuration
public class ScheduleConfig
{
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource)
    {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);

        // quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", "RuoyiScheduler"); // 设置集群实例名称， 在同一个集群内多个节点上该实例名称必须一样
        prop.put("org.quartz.scheduler.instanceId", "AUTO"); // 集群实例Id， 在集群模式下该实例Id必须唯一，AUTO 表示自动生成(计算机名+时间戳）
        // 线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20"); // 线程数
        prop.put("org.quartz.threadPool.threadPriority", "5"); // 线程优先级
        // JobStore配置：使用数据库来存储Job 和Trigger 信息。
        prop.put("org.quartz.jobStore.class", "org.springframework.scheduling.quartz.LocalDataSourceJobStore");
        // 集群配置
        prop.put("org.quartz.jobStore.isClustered", "true"); // 开启集群
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000"); // 心跳间隔
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "10"); // 最大允许的misfire数量
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true"); // 隔离级别

        // sqlserver 启用
        // prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE LOCK_NAME = ?");
        prop.put("org.quartz.jobStore.misfireThreshold", "12000"); // 集群中任务错失执行时间阈值，超过这个时间的任务将被重新加入任务队列
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_"); // 表前缀
        factory.setQuartzProperties(prop);

        factory.setSchedulerName("RuoyiScheduler"); // 集群调度器名称
        // 延时启动
        factory.setStartupDelay(1);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        // 可选，QuartzScheduler
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        // 设置自动启动，默认为true
        factory.setAutoStartup(true);

        return factory;
    }
}
