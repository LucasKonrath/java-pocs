package org.example.quartzpoc.config;

import org.example.quartzpoc.job.CronJob;
import org.example.quartzpoc.job.SimpleJob;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.Map;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean simpleJobDetail() {
        var factory = new JobDetailFactoryBean();
        factory.setJobClass(SimpleJob.class);
        factory.setName("simpleJob");
        factory.setGroup("simple");
        factory.setDurability(true);
        factory.setJobDataAsMap(Map.of("message", "Hello from SimpleJob!"));
        return factory;
    }

    @Bean
    public SimpleTriggerFactoryBean simpleTrigger(JobDetail simpleJobDetail) {
        var factory = new SimpleTriggerFactoryBean();
        factory.setJobDetail(simpleJobDetail);
        factory.setName("simpleTrigger");
        factory.setGroup("simple");
        factory.setRepeatInterval(10_000);
        factory.setRepeatCount(5);
        factory.setStartDelay(2_000);
        return factory;
    }

    @Bean
    public JobDetailFactoryBean cronJobDetail() {
        var factory = new JobDetailFactoryBean();
        factory.setJobClass(CronJob.class);
        factory.setName("cronJob");
        factory.setGroup("cron");
        factory.setDurability(true);
        return factory;
    }

    @Bean
    public CronTriggerFactoryBean cronTrigger(JobDetail cronJobDetail) {
        var factory = new CronTriggerFactoryBean();
        factory.setJobDetail(cronJobDetail);
        factory.setName("cronTrigger");
        factory.setGroup("cron");
        factory.setCronExpression("0/30 * * * * ?");
        return factory;
    }
}
