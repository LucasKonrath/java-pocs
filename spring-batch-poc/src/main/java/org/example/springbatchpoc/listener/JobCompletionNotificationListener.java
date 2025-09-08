package org.example.springbatchpoc.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("!!! JOB STARTED! Time to transform some employees!");
        log.info("Job Name: {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            // Check if output file was created
            File outputFile = new File("output/processed-employees.csv");
            if (outputFile.exists()) {
                log.info("Output file created successfully at: {}", outputFile.getAbsolutePath());
                log.info("File size: {} bytes", outputFile.length());
            } else {
                log.warn("Output file was not created!");
            }

            if (jobExecution.getEndTime() != null && jobExecution.getStartTime() != null) {
                log.info("Job completed in {} ms",
                    java.time.Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).toMillis());
            }
        } else {
            log.error("!!! JOB FAILED with status: {}", jobExecution.getStatus());
            jobExecution.getAllFailureExceptions().forEach(ex ->
                log.error("Failure exception: ", ex));
        }
    }
}
