package org.example.lettucepoc.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Demonstrates the reactive Lettuce API (Project Reactor).
 *
 * Commands return {@link Mono} or {@link Flux} and nothing is executed until
 * something subscribes.  This integrates naturally with Spring WebFlux pipelines.
 */
@Service
public class ReactiveRedisService {

    private static final Logger log = LoggerFactory.getLogger(ReactiveRedisService.class);

    private final RedisClient redisClient;

    public ReactiveRedisService(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public void demo() {
        log.info("=== Reactive API Demo ===");

        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisReactiveCommands<String, String> commands = connection.reactive();

            // --- Mono: single value ---
            Mono<String> setAndGet = commands.set("reactive:greeting", "Hello, Lettuce!")
                    .then(commands.get("reactive:greeting"));

            String value = setAndGet.block();
            log.info("reactive:greeting = {}", value);

            // --- Flux: stream of values from a list ---
            Mono<Long> populateList = commands.del("reactive:queue")
                    .then(commands.rpush("reactive:queue",
                            "item-a", "item-b", "item-c", "item-d"));

            List<String> items = populateList
                    .thenMany(commands.lrange("reactive:queue", 0, -1))
                    .collectList()
                    .block();
            log.info("reactive:queue contents = {}", items);

            // --- Reactive pipeline: filter + transform ---
            commands.del("reactive:scores").block();
            Flux.range(1, 6)
                    .flatMap(i -> commands.zadd("reactive:scores", i * 10.0, "player-" + i))
                    .then(
                        commands.zrangeWithScores("reactive:scores", 0, -1)
                                .filter(sv -> sv.getScore() >= 30.0)
                                .doOnNext(sv -> log.info("Score >= 30: {} -> {}", sv.getValue(), sv.getScore()))
                                .then()
                    )
                    .block();

            // --- Mono.zip: run two commands concurrently, combine results ---
            commands.set("reactive:a", "foo").block();
            commands.set("reactive:b", "bar").block();

            Mono.zip(commands.get("reactive:a"), commands.get("reactive:b"))
                    .doOnNext(tuple -> log.info("zip result: a={}, b={}", tuple.getT1(), tuple.getT2()))
                    .block();

            // Cleanup
            commands.del("reactive:greeting", "reactive:queue",
                         "reactive:scores", "reactive:a", "reactive:b").block();
        }
    }
}
