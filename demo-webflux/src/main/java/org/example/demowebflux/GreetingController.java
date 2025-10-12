package org.example.demowebflux;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api")
public class GreetingController {

    @GetMapping("/hello")
    public Mono<String> hello(@RequestParam(defaultValue = "world") String name) {
        return Mono.just("Hello, " + name + "!");
    }

    @GetMapping(value = "/ticks", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> ticks() {
        return Flux.interval(Duration.ofSeconds(1)).take(5);
    }
}

