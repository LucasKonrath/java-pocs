package org.example.camelpoc.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimerRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:greeting?period=5000")
                .routeId("timer-route")
                .process(exchange -> exchange.getIn().setBody(
                        "Hello from Camel! Time: " + LocalDateTime.now()))
                .to("log:timer-route?showBody=true");
    }
}
