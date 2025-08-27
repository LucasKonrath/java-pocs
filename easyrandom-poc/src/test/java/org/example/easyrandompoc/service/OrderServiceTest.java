package org.example.easyrandompoc.service;

import org.example.easyrandompoc.model.Order;
import org.example.easyrandompoc.model.OrderItem;
import org.example.easyrandompoc.model.OrderStatus;
import org.example.easyrandompoc.model.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;
    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();

        // Configure EasyRandom with custom parameters for better test data
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(456L)
                .objectPoolSize(100)
                .randomizationDepth(3)
                .stringLengthRange(5, 20)
                .collectionSizeRange(1, 5)
                .excludeField(FieldPredicates.named("totalPrice")) // We'll calculate this manually
                .excludeField(FieldPredicates.named("totalAmount")); // We'll calculate this manually

        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("Should return zero for null order")
    void calculateOrderTotal_ShouldReturnZero_WhenOrderIsNull() {
        BigDecimal total = orderService.calculateOrderTotal(null);
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    @DisplayName("Should return zero for order with null items")
    void calculateOrderTotal_ShouldReturnZero_WhenItemsAreNull() {
        Order order = easyRandom.nextObject(Order.class);
        order.setItems(null);

        BigDecimal total = orderService.calculateOrderTotal(order);
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    @DisplayName("Should return zero for order with empty items")
    void calculateOrderTotal_ShouldReturnZero_WhenItemsAreEmpty() {
        Order order = easyRandom.nextObject(Order.class);
        order.setItems(Collections.emptyList());

        BigDecimal total = orderService.calculateOrderTotal(order);
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    @DisplayName("Should calculate correct total for order with items")
    void calculateOrderTotal_ShouldCalculateCorrectTotal_WithValidItems() {
        Order order = easyRandom.nextObject(Order.class);

        OrderItem item1 = new OrderItem();
        item1.setTotalPrice(BigDecimal.valueOf(10.50));

        OrderItem item2 = new OrderItem();
        item2.setTotalPrice(BigDecimal.valueOf(25.75));

        order.setItems(Arrays.asList(item1, item2));

        BigDecimal total = orderService.calculateOrderTotal(order);
        assertEquals(new BigDecimal("36.25"), total);
    }

    @RepeatedTest(10)
    @DisplayName("Should handle random orders consistently")
    void calculateOrderTotal_ShouldHandleRandomOrdersConsistently() {
        Order order = easyRandom.nextObject(Order.class);

        // Set random but valid total prices
        order.getItems().forEach(item ->
            item.setTotalPrice(BigDecimal.valueOf(Math.random() * 100).setScale(2, BigDecimal.ROUND_HALF_UP))
        );

        BigDecimal total = orderService.calculateOrderTotal(order);

        assertNotNull(total);
        assertTrue(total.compareTo(BigDecimal.ZERO) >= 0);
        assertEquals(2, total.scale()); // Should have 2 decimal places
    }

    @Test
    @DisplayName("Should return false for null order when checking cancellation")
    void canCancelOrder_ShouldReturnFalse_WhenOrderIsNull() {
        assertFalse(orderService.canCancelOrder(null));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"PENDING", "CONFIRMED"})
    @DisplayName("Should allow cancellation for PENDING and CONFIRMED orders")
    void canCancelOrder_ShouldReturnTrue_ForCancellableStatuses(OrderStatus status) {
        Order order = easyRandom.nextObject(Order.class);
        order.setStatus(status);

        assertTrue(orderService.canCancelOrder(order));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED", "REFUNDED"})
    @DisplayName("Should not allow cancellation for non-cancellable statuses")
    void canCancelOrder_ShouldReturnFalse_ForNonCancellableStatuses(OrderStatus status) {
        Order order = easyRandom.nextObject(Order.class);
        order.setStatus(status);

        assertFalse(orderService.canCancelOrder(order));
    }

    @Test
    @DisplayName("Should return zero shipping cost for orders over $100")
    void calculateShippingCost_ShouldReturnZero_ForOrdersOver100() {
        Order order = createOrderWithTotal(BigDecimal.valueOf(150.00));

        BigDecimal shippingCost = orderService.calculateShippingCost(order);
        assertEquals(BigDecimal.ZERO, shippingCost);
    }

    @Test
    @DisplayName("Should return $5.99 shipping for orders between $50-$99.99")
    void calculateShippingCost_ShouldReturn599_ForOrdersBetween50And100() {
        Order order = createOrderWithTotal(BigDecimal.valueOf(75.00));

        BigDecimal shippingCost = orderService.calculateShippingCost(order);
        assertEquals(BigDecimal.valueOf(5.99), shippingCost);
    }

    @Test
    @DisplayName("Should return $9.99 shipping for orders under $50")
    void calculateShippingCost_ShouldReturn999_ForOrdersUnder50() {
        Order order = createOrderWithTotal(BigDecimal.valueOf(25.00));

        BigDecimal shippingCost = orderService.calculateShippingCost(order);
        assertEquals(BigDecimal.valueOf(9.99), shippingCost);
    }

    @Test
    @DisplayName("Should return true for express eligible orders")
    void isExpressEligible_ShouldReturnTrue_ForEligibleOrders() {
        Order order = easyRandom.nextObject(Order.class);

        OrderItem item1 = new OrderItem();
        item1.setQuantity(3);
        item1.setTotalPrice(BigDecimal.valueOf(50.00));

        OrderItem item2 = new OrderItem();
        item2.setQuantity(2);
        item2.setTotalPrice(BigDecimal.valueOf(75.00));

        order.setItems(Arrays.asList(item1, item2));

        assertTrue(orderService.isExpressEligible(order));
    }

    @Test
    @DisplayName("Should return false for orders with too many items")
    void isExpressEligible_ShouldReturnFalse_ForOrdersWithTooManyItems() {
        Order order = easyRandom.nextObject(Order.class);

        OrderItem item = new OrderItem();
        item.setQuantity(15); // Over the limit of 10
        item.setTotalPrice(BigDecimal.valueOf(50.00));

        order.setItems(Collections.singletonList(item));

        assertFalse(orderService.isExpressEligible(order));
    }

    @Test
    @DisplayName("Should return false for orders over $200")
    void isExpressEligible_ShouldReturnFalse_ForOrdersOver200() {
        Order order = createOrderWithTotal(BigDecimal.valueOf(250.00));

        // Set quantity to be within limits
        order.getItems().forEach(item -> item.setQuantity(1));

        assertFalse(orderService.isExpressEligible(order));
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"PENDING", "CONFIRMED", "PROCESSING", "SHIPPED"})
    @DisplayName("Should return next status in workflow")
    void getNextStatus_ShouldReturnCorrectNextStatus(OrderStatus currentStatus) {
        OrderStatus nextStatus = orderService.getNextStatus(currentStatus);

        switch (currentStatus) {
            case PENDING:
                assertEquals(OrderStatus.CONFIRMED, nextStatus);
                break;
            case CONFIRMED:
                assertEquals(OrderStatus.PROCESSING, nextStatus);
                break;
            case PROCESSING:
                assertEquals(OrderStatus.SHIPPED, nextStatus);
                break;
            case SHIPPED:
                assertEquals(OrderStatus.DELIVERED, nextStatus);
                break;
        }
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"DELIVERED", "CANCELLED", "REFUNDED"})
    @DisplayName("Should return same status for terminal statuses")
    void getNextStatus_ShouldReturnSameStatus_ForTerminalStatuses(OrderStatus terminalStatus) {
        OrderStatus nextStatus = orderService.getNextStatus(terminalStatus);
        assertEquals(terminalStatus, nextStatus);
    }

    @Test
    @DisplayName("Should return true for recent orders")
    void isOrderRecent_ShouldReturnTrue_ForRecentOrders() {
        Order order = easyRandom.nextObject(Order.class);
        order.setCreatedAt(LocalDateTime.now().minusDays(1));

        assertTrue(orderService.isOrderRecent(order));
    }

    @Test
    @DisplayName("Should return false for old orders")
    void isOrderRecent_ShouldReturnFalse_ForOldOrders() {
        Order order = easyRandom.nextObject(Order.class);
        order.setCreatedAt(LocalDateTime.now().minusDays(5));

        assertFalse(orderService.isOrderRecent(order));
    }

    @Test
    @DisplayName("Should return false for null order")
    void isOrderRecent_ShouldReturnFalse_ForNullOrder() {
        assertFalse(orderService.isOrderRecent(null));
    }

    // Helper method to create orders with specific totals
    private Order createOrderWithTotal(BigDecimal targetTotal) {
        Order order = easyRandom.nextObject(Order.class);

        OrderItem item = new OrderItem();
        item.setQuantity(1);
        item.setTotalPrice(targetTotal);

        order.setItems(Collections.singletonList(item));
        return order;
    }
}
