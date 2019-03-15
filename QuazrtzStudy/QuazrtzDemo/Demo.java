package pri.lr.quartz_study.demo;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

import static org.quartz.JobBuilder.newJob;

public class Demo {
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        // 1. 创建调度器Scheduler
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 2. 创建JobDetail实例，并与自定义的任务类（PrintJob）绑定
        JobDetail jobDetail = JobBuilder.newJob(PrintJob.class).withIdentity("job1", "group1").build();

        // 3. 创建Trigger实例，每秒执行一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever())// 每1秒执行一次
                .build(); // 一直执行
        // 4. 执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("scheduler start:");
        scheduler.start();

        //睡眠
        TimeUnit.MINUTES.sleep(1);
        scheduler.shutdown();;
        System.out.println("scheduler shutdown");
    }
}
