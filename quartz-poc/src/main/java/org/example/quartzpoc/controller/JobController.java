package org.example.quartzpoc.controller;

import org.example.quartzpoc.job.SimpleJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final Scheduler scheduler;

    public JobController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostMapping("/trigger")
    public Map<String, String> triggerJob(@RequestParam(defaultValue = "Manually triggered") String message) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("manual-" + System.currentTimeMillis(), "manual")
                .usingJobData("message", message)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("manual-trigger-" + System.currentTimeMillis(), "manual")
                .startNow()
                .build();

        scheduler.scheduleJob(job, trigger);
        return Map.of("status", "triggered", "message", message);
    }

    @GetMapping
    public List<Map<String, String>> listJobs() throws SchedulerException {
        List<Map<String, String>> jobs = new ArrayList<>();
        for (String group : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
                Map<String, String> jobInfo = new HashMap<>();
                jobInfo.put("name", jobKey.getName());
                jobInfo.put("group", jobKey.getGroup());
                jobInfo.put("class", scheduler.getJobDetail(jobKey).getJobClass().getSimpleName());
                jobs.add(jobInfo);
            }
        }
        return jobs;
    }

    @DeleteMapping("/{name}")
    public Map<String, String> pauseJob(@PathVariable String name) throws SchedulerException {
        for (String group : scheduler.getJobGroupNames()) {
            JobKey jobKey = new JobKey(name, group);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                return Map.of("status", "paused", "job", name, "group", group);
            }
        }
        return Map.of("status", "not_found", "job", name);
    }
}
