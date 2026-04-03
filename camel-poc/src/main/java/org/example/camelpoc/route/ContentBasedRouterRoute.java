package org.example.camelpoc.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ContentBasedRouterRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:orders")
                .routeId("content-based-router")
                .log("Received order: ${body}")
                .choice()
                    .when(header("orderType").isEqualTo("priority"))
                        .to("direct:priority")
                    .when(header("orderType").isEqualTo("standard"))
                        .to("direct:standard")
                    .otherwise()
                        .to("direct:unknown")
                .end();

        from("direct:priority")
                .routeId("priority-route")
                .log("PRIORITY order processed: ${body}");

        from("direct:standard")
                .routeId("standard-route")
                .log("STANDARD order processed: ${body}");

        from("direct:unknown")
                .routeId("unknown-route")
                .log("UNKNOWN order type: ${body}");
    }
}
