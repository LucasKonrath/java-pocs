package org.example.quartzpoc.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

public class CronJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("[CronJob] %s | Running periodic cleanup task...".formatted(LocalDateTime.now()));
        System.out.println("[CronJob] Cleaning up expired sessions...");
        System.out.println("[CronJob] Cleanup complete.");
    }
}
