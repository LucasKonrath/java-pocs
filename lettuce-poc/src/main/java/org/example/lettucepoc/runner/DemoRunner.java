package org.example.lettucepoc.runner;

import org.example.lettucepoc.service.AsyncRedisService;
import org.example.lettucepoc.service.ReactiveRedisService;
import org.example.lettucepoc.service.SpringDataRedisService;
import org.example.lettucepoc.service.SyncRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runs all Lettuce demos in sequence on startup.
 *
 * Start Redis locally before running:
 *   docker run --rm -p 6379:6379 redis:7-alpine
 */
@Component
public class DemoRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoRunner.class);

    private final SyncRedisService syncService;
    private final AsyncRedisService asyncService;
    private final ReactiveRedisService reactiveService;
    private final SpringDataRedisService springDataService;

    public DemoRunner(SyncRedisService syncService,
                      AsyncRedisService asyncService,
                      ReactiveRedisService reactiveService,
                      SpringDataRedisService springDataService) {
        this.syncService = syncService;
        this.asyncService = asyncService;
        this.reactiveService = reactiveService;
        this.springDataService = springDataService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Lettuce POC demos...");

        syncService.demo();
        asyncService.demo();
        reactiveService.demo();
        springDataService.demo();

        log.info("All demos completed.");
    }
}
