package org.example.lettucepoc.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Demonstrates the synchronous (blocking) Lettuce API.
 *
 * The synchronous API is the simplest to use — every command blocks the calling
 * thread until Redis responds. Good for scripts and simple use-cases; avoid in
 * reactive/async pipelines.
 */
@Service
public class SyncRedisService {

    private static final Logger log = LoggerFactory.getLogger(SyncRedisService.class);

    private final RedisClient redisClient;

    public SyncRedisService(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public void demo() {
        log.info("=== Sync API Demo ===");

        // Connections are thread-safe and can be shared, but NOT across threads
        // without connection pooling. For this POC we open/close per demo.
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisCommands<String, String> commands = connection.sync();

            // --- String commands ---
            commands.set("user:1:name", "Alice");
            commands.set("user:1:age", "30");
            log.info("SET user:1:name = {}", commands.get("user:1:name"));
            log.info("SET user:1:age  = {}", commands.get("user:1:age"));

            // TTL via SETEX
            commands.setex("session:abc", 60, "token-xyz");
            log.info("TTL session:abc = {} seconds", commands.ttl("session:abc"));

            // --- Increment / Decrement ---
            commands.set("counter", "0");
            commands.incr("counter");
            commands.incrby("counter", 4);
            log.info("counter after INCR + INCRBY 4 = {}", commands.get("counter"));

            // --- Hash commands ---
            commands.hset("product:1", Map.of(
                    "name", "Widget",
                    "price", "9.99",
                    "stock", "100"
            ));
            log.info("HGET product:1 name = {}", commands.hget("product:1", "name"));
            log.info("HGETALL product:1  = {}", commands.hgetall("product:1"));

            // --- List commands ---
            commands.del("tasks");
            commands.rpush("tasks", "task-1", "task-2", "task-3");
            commands.lpush("tasks", "task-0");
            List<String> tasks = commands.lrange("tasks", 0, -1);
            log.info("tasks list = {}", tasks);

            // --- Set commands ---
            commands.sadd("tags", "java", "redis", "lettuce", "spring");
            log.info("tags members  = {}", commands.smembers("tags"));
            log.info("tags contains 'redis' = {}", commands.sismember("tags", "redis"));

            // Cleanup
            commands.del("user:1:name", "user:1:age", "session:abc",
                         "counter", "product:1", "tasks", "tags");
        }
    }
}
