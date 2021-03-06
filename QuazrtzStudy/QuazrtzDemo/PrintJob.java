package pri.lr.quartz_study.demo;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PrintJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String printTime = new SimpleDateFormat("yy-MM-dd HH-mm-ss").format(new Date());
        System.out.println("now:" + printTime + ", prints:hello job-" + new Random().nextInt(100));
    }
}
