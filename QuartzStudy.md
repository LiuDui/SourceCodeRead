## Quartz简单使用
#### 简单Demo
> 1. 添加依赖：在gradle中添加如下：
```java
dependencies {
      implementation group: 'org.quartz-scheduler', name: 'quartz', version: '2.3.0'
}
```
2. 创建任务：通过实现接口：
```java
public class PrintJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String printTime = new SimpleDateFormat("yy-MM-dd HH-mm-ss").format(new Date());
        System.out.println("now:" + printTime + ", prints:hello job-" + new Random().nextInt(100));
    }
}
```
打印当前时间。
3. 构建触发器：Trigger，并通过Scheduler 将job和触发器连接起来。
```java
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
        scheduler.shutdown();
        System.out.println("scheduler shutdown");
    }
}
```
(如果主线程结束，那么这个定时任务线程也会结束)
输出结果：
```java
scheduler start:
now:19-03-15 17-51-11, prints:hello job-93
now:19-03-15 17-51-12, prints:hello job-93
now:19-03-15 17-51-13, prints:hello job-8
...
now:19-03-15 17-52-10, prints:hello job-61
now:19-03-15 17-52-11, prints:hello job-87
scheduler shutdown
```
#### 配置文件
