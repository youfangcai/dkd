package com.dkd.quartz.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import com.dkd.common.constant.Constants;
import com.dkd.common.constant.ScheduleConstants;
import com.dkd.common.exception.job.TaskException;
import com.dkd.common.exception.job.TaskException.Code;
import com.dkd.common.utils.StringUtils;
import com.dkd.common.utils.spring.SpringUtils;
import com.dkd.quartz.domain.SysJob;

/**
 * 定时任务工具类
 * 
 * @author ruoyi
 *
 */
public class ScheduleUtils
{
    /**
     * 得到quartz任务类
     *
     * @param sysJob 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(SysJob sysJob)
    {
        boolean isConcurrent = "0".equals(sysJob.getConcurrent()); // 判断是否并发执行
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup)
    {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(Long jobId, String jobGroup)
    {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob job) throws SchedulerException, TaskException
    {
        /* 1. 判断是否并发执行，并获得对应的任务类 */
        Class<? extends Job> jobClass = getQuartzJobClass(job); // 判断是否并发执行，并获得对应的任务执行类, 继承了 Job 类
        /* 2. 构建job信息，（通过 JobBuilder 创建 JobDetail对象） */
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobGroup)).build();

        // 表达式调度构建器， 设置复杂的定时任务策略
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder); // 设置定时任务策略

        /* 3. 按新的cronExpression表达式构建一个新的trigger， （通过 TriggerBuilder 构建 Trigger 类对象） */
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder).build(); // 根据Corne表达式执行Scheduler

        // 放入参数，运行时的方法可以获取job
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);

        // 判断是否存在
        if (scheduler.checkExists(getJobKey(jobId, jobGroup)))
        {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        }

        // 判断任务是否过期
        if (StringUtils.isNotNull(CronUtils.getNextExecution(job.getCronExpression())))
        {
            /* 4. 设置执行的调度任务， （将 jobDetail 和 trigger 添加到 scheduler 调度器中） */
            scheduler.scheduleJob(jobDetail, trigger); // 默认是已经开始执行了，虽然是暂停状态
        }

        // 判断是否是暂停任务
        if (job.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue()))
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob job, CronScheduleBuilder cb)
            throws TaskException
    {
        switch (job.getMisfirePolicy())
        {
            case ScheduleConstants.MISFIRE_DEFAULT: // 默认正常策略
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:  /** 立即触发执行 */
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:    /** 触发一次执行 */
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:     /** 不触发立即执行 */
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy '" + job.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks", Code.CONFIG_ERROR);
        }
    }

    /**
     * 检查包名是否为白名单配置
     * 
     * @param invokeTarget 目标字符串
     * @return 结果
     */
    public static boolean whiteList(String invokeTarget)
    {
        String packageName = StringUtils.substringBefore(invokeTarget, "(");
        int count = StringUtils.countMatches(packageName, ".");
        if (count > 1)
        {
            return StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.JOB_WHITELIST_STR);
        }
        Object obj = SpringUtils.getBean(StringUtils.split(invokeTarget, ".")[0]);
        String beanPackageName = obj.getClass().getPackage().getName();
        return StringUtils.containsAnyIgnoreCase(beanPackageName, Constants.JOB_WHITELIST_STR)
                && !StringUtils.containsAnyIgnoreCase(beanPackageName, Constants.JOB_ERROR_STR);
    }
}
