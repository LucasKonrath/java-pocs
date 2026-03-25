package org.example.lettucepoc.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Demonstrates the asynchronous Lettuce API.
 *
 * Every command returns a {@link RedisFuture} (which extends {@link CompletableFuture}).
 * Commands are dispatched immediately and execute concurrently — the thread is NOT
 * blocked while Redis processes them.  Results are composed with thenApply/thenCompose
 * or joined at the end.
 */
@Service
public class AsyncRedisService {

    private static final Logger log = LoggerFactory.getLogger(AsyncRedisService.class);

    private final RedisClient redisClient;

    public AsyncRedisService(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public void demo() throws ExecutionException, InterruptedException {
        log.info("=== Async API Demo ===");

        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisAsyncCommands<String, String> commands = connection.async();

            // --- Fire multiple commands concurrently ---
            // Commands are sent to Redis without waiting for each response.
            RedisFuture<String> setFirst  = commands.set("async:first",  "100");
            RedisFuture<String> setSecond = commands.set("async:second", "200");
            RedisFuture<String> setThird  = commands.set("async:third",  "300");

            // Wait for all SETs to complete before reading
            CompletableFuture.allOf(setFirst, setSecond, setThird).join();

            // --- Pipeline: chain GET after SET via thenCompose ---
            String result = commands.set("async:chained", "hello")
                    .thenCompose(ok -> commands.get("async:chained"))
                    .thenApply(value -> "chained GET returned: " + value)
                    .get();
            log.info(result);

            // --- Parallel GETs composed together ---
            RedisFuture<String> getFirst  = commands.get("async:first");
            RedisFuture<String> getSecond = commands.get("async:second");
            RedisFuture<String> getThird  = commands.get("async:third");

            CompletableFuture.allOf(getFirst, getSecond, getThird)
                    .thenRun(() -> {
                        try {
                            log.info("Parallel GET results: first={}, second={}, third={}",
                                    getFirst.get(), getSecond.get(), getThird.get());
                        } catch (Exception e) {
                            log.error("Error reading parallel results", e);
                        }
                    })
                    .join();

            // --- Async INCR pipeline ---
            commands.set("async:counter", "0").get();
            RedisFuture<Long> incr1 = commands.incr("async:counter");
            RedisFuture<Long> incr2 = commands.incr("async:counter");
            RedisFuture<Long> incr3 = commands.incr("async:counter");
            CompletableFuture.allOf(incr1, incr2, incr3).join();
            log.info("Counter after 3 async INCRs = {}", commands.get("async:counter").get());

            // Cleanup
            commands.del("async:first", "async:second", "async:third",
                         "async:chained", "async:counter").get();
        }
    }
}
