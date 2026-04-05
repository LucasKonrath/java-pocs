package org.example.guavapoc.event;

import com.google.common.eventbus.Subscribe;

public class OrderEventListener {

    @Subscribe
    public void onOrderEvent(OrderEvent event) {
        System.out.println("  Received order: id=%s, amount=%.2f"
                .formatted(event.orderId(), event.amount()));
    }
}
