package org.example.quartzpoc.job;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.time.LocalDateTime;

public class ChainedJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("[ChainedJob] %s | Step 1 - Data export completed".formatted(LocalDateTime.now()));

        try {
            Scheduler scheduler = context.getScheduler();
            JobDetail followUpJob = JobBuilder.newJob(SimpleJob.class)
                    .withIdentity("followUpJob", "chain")
                    .usingJobData("message", "Triggered by ChainedJob as follow-up")
                    .build();

            Trigger nowTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("followUpTrigger", "chain")
                    .startNow()
                    .build();

            if (!scheduler.checkExists(followUpJob.getKey())) {
                scheduler.scheduleJob(followUpJob, nowTrigger);
                System.out.println("[ChainedJob] Follow-up job scheduled.");
            }
        } catch (SchedulerException e) {
            throw new JobExecutionException("Failed to chain follow-up job", e);
        }
    }
}
