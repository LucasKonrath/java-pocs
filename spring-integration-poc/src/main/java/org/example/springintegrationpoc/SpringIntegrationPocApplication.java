package org.example.springintegrationpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@IntegrationComponentScan
public class SpringIntegrationPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationPocApplication.class, args);
    }

}
