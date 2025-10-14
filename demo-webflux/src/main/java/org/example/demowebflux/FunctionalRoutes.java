package org.example.demowebflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class FunctionalRoutes {

    @Bean
    public RouterFunction<ServerResponse> demoRoutes() {
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
                .build();
    }
}

