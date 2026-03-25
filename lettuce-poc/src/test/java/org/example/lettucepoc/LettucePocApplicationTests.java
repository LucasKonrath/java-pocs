package org.example.lettucepoc;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Mono;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LettucePocApplicationTests {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ReactiveStringRedisTemplate reactiveTemplate;

    @AfterEach
    void cleanup() {
        stringRedisTemplate.delete(java.util.Set.of(
                "test:sync", "test:async", "test:reactive", "test:sdr"
        ));
    }

    @Test
    void syncApiSetAndGet() {
        try (var connection = redisClient.connect()) {
            RedisCommands<String, String> commands = connection.sync();
            commands.set("test:sync", "sync-value");
            assertThat(commands.get("test:sync")).isEqualTo("sync-value");
        }
    }

    @Test
    void asyncApiSetAndGet() throws Exception {
        try (var connection = redisClient.connect()) {
            var commands = connection.async();
            commands.set("test:async", "async-value").get();
            String value = commands.get("test:async").get();
            assertThat(value).isEqualTo("async-value");
        }
    }

    @Test
    void reactiveApiSetAndGet() {
        try (var connection = redisClient.connect()) {
            var commands = connection.reactive();

            Mono<String> pipeline = commands.set("test:reactive", "reactive-value")
                    .then(commands.get("test:reactive"));

            StepVerifier.create(pipeline)
                    .expectNext("reactive-value")
                    .verifyComplete();
        }
    }

    @Test
    void springDataRedisSetAndGet() {
        stringRedisTemplate.opsForValue().set("test:sdr", "sdr-value", Duration.ofMinutes(1));
        assertThat(stringRedisTemplate.opsForValue().get("test:sdr")).isEqualTo("sdr-value");
    }

    @Test
    void reactiveTemplateSetAndGet() {
        Mono<String> pipeline = reactiveTemplate.opsForValue()
                .set("test:reactive", "reactive-template-value")
                .then(reactiveTemplate.opsForValue().get("test:reactive"));

        StepVerifier.create(pipeline)
                .expectNext("reactive-template-value")
                .verifyComplete();
    }
}
