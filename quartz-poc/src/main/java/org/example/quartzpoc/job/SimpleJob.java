package org.example.quartzpoc.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

public class SimpleJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String message = context.getMergedJobDataMap().getString("message");
        System.out.println("[SimpleJob] %s | message: %s"
                .formatted(LocalDateTime.now(), message != null ? message : "no message"));
    }
}
