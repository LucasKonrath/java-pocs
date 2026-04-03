package org.example.camelpoc.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileProcessingRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("file:data/input?noop=true")
                .routeId("file-processing-route")
                .log("Processing file: ${header.CamelFileName}")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    exchange.getIn().setBody(body.toUpperCase());
                })
                .to("file:data/output")
                .log("File ${header.CamelFileName} processed and written to output.");
    }
}
