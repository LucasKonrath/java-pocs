package org.example.lettucepoc.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class LettuceConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    /**
     * Raw Lettuce RedisClient — gives direct access to sync/async/reactive APIs
     * without going through Spring Data abstractions.
     */
    @Bean(destroyMethod = "shutdown")
    public RedisClient redisClient() {
        return RedisClient.create(RedisURI.builder()
                .withHost(host)
                .withPort(port)
                .build());
    }

    /**
     * Spring Data StringRedisTemplate — higher-level abstraction over Lettuce,
     * handles (de)serialization and connection management automatically.
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(
            org.springframework.data.redis.connection.RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    /**
     * Reactive variant of StringRedisTemplate for non-blocking access via Reactor.
     */
    @Bean
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(
            ReactiveRedisConnectionFactory factory) {
        return new ReactiveStringRedisTemplate(factory);
    }
}
