package org.example.camelpoc.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.camelpoc.model.Order;
import org.springframework.stereotype.Component;

@Component("orderValidator")
public class OrderValidationProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Order order = exchange.getIn().getBody(Order.class);

        if (order == null) {
            exchange.getIn().setHeader("validationError", "Order is null");
            return;
        }

        if (order.getQuantity() <= 0) {
            exchange.getIn().setHeader("validationError", "Quantity must be positive");
            return;
        }

        if (order.getProduct() == null || order.getProduct().isBlank()) {
            exchange.getIn().setHeader("validationError", "Product is required");
            return;
        }

        exchange.getIn().setHeader("validated", true);
        System.out.println("[OrderValidator] Order %s validated successfully".formatted(order.getOrderId()));
    }
}
