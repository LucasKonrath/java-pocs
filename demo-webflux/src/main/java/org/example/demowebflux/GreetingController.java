package org.example.demowebflux;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
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

    @PostMapping("/greet")
    public Mono<String> greet(@Valid @RequestBody GreetingRequest request) {
        return Mono.just("Hello, " + request.getName() + "!");
    }

    @GetMapping("/upper")
    public Mono<String> upper(@RequestParam String text) {
        return Mono.just(text).map(String::toUpperCase);
    }

    @GetMapping(value = "/numbers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> numbers(@RequestParam(defaultValue = "5") int count) {
        return Flux.range(1, count).delayElements(Duration.ofMillis(500));
    }

    @GetMapping("/items/{id}")
    public Mono<String> getById(@PathVariable String id) {
        if ("0".equals(id)) {
            throw new IllegalArgumentException("id must not be 0");
        }
        return Mono.just("Item: " + id);
    }
}
