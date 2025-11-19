package org.example.demowebflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class FunctionalRoutes {

    @Bean(destroyMethod = "close")
    public ExecutorService virtualThreadExecutor() {
        // Create an executor that uses virtual threads (Java 21+)
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Scheduler virtualThreadScheduler(ExecutorService virtualThreadExecutor) {
        return Schedulers.fromExecutorService(virtualThreadExecutor);
    }

    @Bean
    public RouterFunction<ServerResponse> demoRoutes(Scheduler virtualThreadScheduler) {
        return RouterFunctions.route()
                .GET("/api/fn/echo", request -> {
                    String text = request.queryParam("text").orElse("world");
                    return ServerResponse.ok().bodyValue("Echo: " + text);
                })
                .GET("/api/fn/stream", request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(Flux.interval(Duration.ofMillis(300)).take(10), Long.class))
                .POST("/api/fn/reverse", request -> request.bodyToMono(String.class)
                        .map(s -> new StringBuilder(s).reverse().toString())
                        .flatMap(rev -> ServerResponse.ok().bodyValue(rev)))
                // new route demonstrating virtual threads for blocking work
                .GET("/api/fn/virtual", request -> {
                    Mono<String> m = Mono.fromCallable(() -> {
                        // simulate blocking or legacy code
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        return "Handled on: " + Thread.currentThread();
                    }).subscribeOn(virtualThreadScheduler);

                    return ServerResponse.ok().body(m, String.class);
                })
                .build();
    }
}
