package org.example.springbatchpoc.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchJobRunner implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job processEmployeeJob;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("Starting the Employee Processing Batch Job...");

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(processEmployeeJob, jobParameters);

            System.out.println("Batch job completed with status: " + jobExecution.getStatus());

        } catch (Exception e) {
            System.err.println("Error running batch job: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
