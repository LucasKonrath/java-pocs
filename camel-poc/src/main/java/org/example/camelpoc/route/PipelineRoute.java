package org.example.camelpoc.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class PipelineRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:pipeline")
                .routeId("pipeline-route")
                .log("Pipeline START: ${body}")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    exchange.getIn().setBody(body + " -> [step1:validated]");
                })
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    exchange.getIn().setBody(body + " -> [step2:enriched]");
                })
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    exchange.getIn().setBody(body + " -> [step3:transformed]");
                })
                .log("Pipeline END: ${body}");

        from("direct:multicast-demo")
                .routeId("multicast-route")
                .log("Multicast START: ${body}")
                .multicast()
                    .to("direct:channel-a", "direct:channel-b")
                .end();

        from("direct:channel-a")
                .routeId("channel-a")
                .log("Channel A received: ${body}");

        from("direct:channel-b")
                .routeId("channel-b")
                .log("Channel B received: ${body}");
    }
}
