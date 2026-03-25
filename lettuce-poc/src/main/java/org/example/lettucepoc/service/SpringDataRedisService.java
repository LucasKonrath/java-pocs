package org.example.lettucepoc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * Demonstrates the Spring Data Redis abstraction backed by Lettuce.
 *
 * Spring Data Redis wraps Lettuce (or Jedis) with a higher-level API:
 * - Connection management is handled for you (pooling, reconnect).
 * - Keys/values are serialized/deserialized automatically.
 * - Supports both imperative ({@link StringRedisTemplate}) and reactive
 *   ({@link ReactiveStringRedisTemplate}) styles.
 */
@Service
public class SpringDataRedisService {

    private static final Logger log = LoggerFactory.getLogger(SpringDataRedisService.class);

    private final StringRedisTemplate redisTemplate;
    private final ReactiveStringRedisTemplate reactiveTemplate;

    public SpringDataRedisService(StringRedisTemplate redisTemplate,
                                  ReactiveStringRedisTemplate reactiveTemplate) {
        this.redisTemplate = redisTemplate;
        this.reactiveTemplate = reactiveTemplate;
    }

    public void demo() {
        log.info("=== Spring Data Redis Demo ===");

        // --- ValueOperations (String) ---
        var valueOps = redisTemplate.opsForValue();
        valueOps.set("sdr:key", "spring-data-value", Duration.ofMinutes(5));
        log.info("sdr:key = {}", valueOps.get("sdr:key"));

        // --- HashOperations ---
        var hashOps = redisTemplate.opsForHash();
        hashOps.putAll("sdr:user:10", Map.of(
                "name", "Bob",
                "email", "bob@example.com",
                "role", "admin"
        ));
        log.info("sdr:user:10 name  = {}", hashOps.get("sdr:user:10", "name"));
        log.info("sdr:user:10 all   = {}", hashOps.entries("sdr:user:10"));

        // --- ListOperations ---
        var listOps = redisTemplate.opsForList();
        redisTemplate.delete("sdr:events");
        listOps.rightPushAll("sdr:events", "login", "view", "purchase");
        log.info("sdr:events = {}", listOps.range("sdr:events", 0, -1));

        // --- SetOperations ---
        var setOps = redisTemplate.opsForSet();
        setOps.add("sdr:permissions", "READ", "WRITE", "DELETE");
        log.info("sdr:permissions = {}", setOps.members("sdr:permissions"));

        // --- ZSetOperations (Sorted Set) ---
        var zsetOps = redisTemplate.opsForZSet();
        zsetOps.add("sdr:leaderboard", "Alice", 95.0);
        zsetOps.add("sdr:leaderboard", "Bob",   87.0);
        zsetOps.add("sdr:leaderboard", "Carol",  92.0);
        log.info("sdr:leaderboard (desc) = {}",
                zsetOps.reverseRangeWithScores("sdr:leaderboard", 0, -1));

        // --- Reactive Spring Data Redis ---
        Mono<String> reactivePipeline = reactiveTemplate.opsForValue()
                .set("sdr:reactive", "works!")
                .then(reactiveTemplate.opsForValue().get("sdr:reactive"));

        log.info("sdr:reactive = {}", reactivePipeline.block());

        // Cleanup
        redisTemplate.delete(java.util.Set.of(
                "sdr:key", "sdr:user:10", "sdr:events",
                "sdr:permissions", "sdr:leaderboard", "sdr:reactive"
        ));
    }
}
