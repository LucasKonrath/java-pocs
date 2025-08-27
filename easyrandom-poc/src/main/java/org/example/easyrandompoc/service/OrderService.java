package org.example.easyrandompoc.service;

import org.example.easyrandompoc.model.Order;
import org.example.easyrandompoc.model.OrderItem;
import org.example.easyrandompoc.model.OrderStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    public BigDecimal calculateOrderTotal(Order order) {
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean canCancelOrder(Order order) {
        if (order == null || order.getStatus() == null) {
            return false;
        }

        OrderStatus status = order.getStatus();
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    public BigDecimal calculateShippingCost(Order order) {
        if (order == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = calculateOrderTotal(order);

        if (total.compareTo(BigDecimal.valueOf(100)) >= 0) {
            return BigDecimal.ZERO; // Free shipping for orders over $100
        } else if (total.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return BigDecimal.valueOf(5.99); // $5.99 shipping for orders $50-$99.99
        } else {
            return BigDecimal.valueOf(9.99); // $9.99 shipping for orders under $50
        }
    }

    public boolean isExpressEligible(Order order) {
        if (order == null || order.getItems() == null) {
            return false;
        }

        int totalQuantity = order.getItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        BigDecimal total = calculateOrderTotal(order);

        return totalQuantity <= 10 && total.compareTo(BigDecimal.valueOf(200)) <= 0;
    }

    public OrderStatus getNextStatus(OrderStatus currentStatus) {
        if (currentStatus == null) {
            return OrderStatus.PENDING;
        }

        switch (currentStatus) {
            case PENDING:
                return OrderStatus.CONFIRMED;
            case CONFIRMED:
                return OrderStatus.PROCESSING;
            case PROCESSING:
                return OrderStatus.SHIPPED;
            case SHIPPED:
                return OrderStatus.DELIVERED;
            default:
                return currentStatus; // No transition for CANCELLED, REFUNDED, DELIVERED
        }
    }

    public boolean isOrderRecent(Order order) {
        if (order == null || order.getCreatedAt() == null) {
            return false;
        }

        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        return order.getCreatedAt().isAfter(threeDaysAgo);
    }
}
