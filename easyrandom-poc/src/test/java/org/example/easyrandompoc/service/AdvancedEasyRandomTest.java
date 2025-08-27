package org.example.easyrandompoc.service;

import org.example.easyrandompoc.model.Order;
import org.example.easyrandompoc.model.OrderItem;
import org.example.easyrandompoc.model.OrderStatus;
import org.example.easyrandompoc.model.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LocalDateRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Advanced EasyRandom Features and Property-Based Testing")
class AdvancedEasyRandomTest {

    private EasyRandom basicRandom;
    private EasyRandom customRandom;
    private UserService userService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        orderService = new OrderService();

        // Basic EasyRandom configuration
        basicRandom = new EasyRandom();

        // Advanced EasyRandom configuration with custom randomizers
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(789L)
                .objectPoolSize(50)
                .randomizationDepth(4)
                .stringLengthRange(3, 20)
                .collectionSizeRange(1, 5)
                // Custom randomizers for specific fields
                .randomize(FieldPredicates.named("email"), new StringRandomizer(10, 20, 123L))
                .randomize(FieldPredicates.named("age"), new IntegerRangeRandomizer(18, 80, 456L))
                .randomize(FieldPredicates.named("birthDate"),
                    new LocalDateRangeRandomizer(LocalDate.of(1940, 1, 1), LocalDate.of(2005, 12, 31), 789L))
                // Exclude fields that we want to set manually
                .excludeField(FieldPredicates.named("totalPrice"))
                .excludeField(FieldPredicates.named("totalAmount"))
                .excludeField(FieldPredicates.named("createdAt"))
                .excludeField(FieldPredicates.named("updatedAt"));

        customRandom = new EasyRandom(parameters);
    }

    @RepeatedTest(50)
    @DisplayName("Property: User discount should always be between 0% and 15%")
    void propertyTest_UserDiscountShouldAlwaysBeInValidRange() {
        // Generate random user
        User user = customRandom.nextObject(User.class);
        user.setActive(true); // Ensure user is active to get discount

        // Apply the business logic
        double discount = userService.calculateDiscount(user);

        // Verify the property holds for all inputs
        assertTrue(discount >= 0.0 && discount <= 0.15,
                String.format("Discount %.3f is outside valid range [0.0, 0.15] for user: %s", discount, user));
    }

    @RepeatedTest(30)
    @DisplayName("Property: Order total should equal sum of item totals")
    void propertyTest_OrderTotalShouldEqualSumOfItems() {
        // Generate random order
        Order order = customRandom.nextObject(Order.class);

        // Set realistic item prices
        order.getItems().forEach(item -> {
            BigDecimal unitPrice = BigDecimal.valueOf(Math.random() * 100 + 1).setScale(2, BigDecimal.ROUND_HALF_UP);
            int quantity = (int) (Math.random() * 10) + 1;
            item.setQuantity(quantity);
            item.setUnitPrice(unitPrice);
            item.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        });

        // Calculate expected total manually
        BigDecimal expectedTotal = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP);

        // Test the service method
        BigDecimal actualTotal = orderService.calculateOrderTotal(order);

        assertEquals(expectedTotal, actualTotal,
                String.format("Order total mismatch. Expected: %s, Actual: %s, Order: %s",
                        expectedTotal, actualTotal, order));
    }

    @RepeatedTest(40)
    @DisplayName("Property: Shipping cost should follow business rules")
    void propertyTest_ShippingCostShouldFollowBusinessRules() {
        // Generate random order with controlled total
        Order order = customRandom.nextObject(Order.class);

        // Set a random but controlled total amount
        BigDecimal randomTotal = BigDecimal.valueOf(Math.random() * 200).setScale(2, BigDecimal.ROUND_HALF_UP);
        OrderItem singleItem = new OrderItem();
        singleItem.setTotalPrice(randomTotal);
        order.setItems(List.of(singleItem));

        BigDecimal shippingCost = orderService.calculateShippingCost(order);

        // Verify shipping rules
        if (randomTotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
            assertEquals(BigDecimal.ZERO, shippingCost,
                    "Free shipping should apply for orders >= $100. Total: " + randomTotal);
        } else if (randomTotal.compareTo(BigDecimal.valueOf(50)) >= 0) {
            assertEquals(BigDecimal.valueOf(5.99), shippingCost,
                    "$5.99 shipping should apply for orders $50-$99.99. Total: " + randomTotal);
        } else {
            assertEquals(BigDecimal.valueOf(9.99), shippingCost,
                    "$9.99 shipping should apply for orders < $50. Total: " + randomTotal);
        }
    }

    @RepeatedTest(25)
    @DisplayName("Property: Express eligibility should be consistent")
    void propertyTest_ExpressEligibilityShouldBeConsistent() {
        Order order = customRandom.nextObject(Order.class);

        // Set controlled quantities and prices
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            int quantity = (int) (Math.random() * 5) + 1; // 1-5 items
            BigDecimal price = BigDecimal.valueOf(Math.random() * 50 + 10).setScale(2, BigDecimal.ROUND_HALF_UP);

            item.setQuantity(quantity);
            item.setTotalPrice(price);

            totalQuantity += quantity;
            totalAmount = totalAmount.add(price);
        }

        boolean isEligible = orderService.isExpressEligible(order);

        // Verify the business logic
        boolean shouldBeEligible = totalQuantity <= 10 && totalAmount.compareTo(BigDecimal.valueOf(200)) <= 0;

        assertEquals(shouldBeEligible, isEligible,
                String.format("Express eligibility mismatch. Total qty: %d, Total amount: %s, Should be eligible: %b, Is eligible: %b",
                        totalQuantity, totalAmount, shouldBeEligible, isEligible));
    }

    @Test
    @DisplayName("Bulk data generation for stress testing")
    void demonstrateBulkDataGeneration() {
        // Generate a large number of users for stress testing
        List<User> users = IntStream.range(0, 1000)
                .mapToObj(i -> customRandom.nextObject(User.class))
                .toList();

        assertEquals(1000, users.size());

        // Verify all users have valid data structure
        users.forEach(user -> {
            assertNotNull(user.getName());
            assertNotNull(user.getEmail());
            assertTrue(user.getAge() >= 18 && user.getAge() <= 80); // Based on our custom randomizer
            assertNotNull(user.getBirthDate());
        });

        // Test business logic on bulk data
        long validUsers = users.stream()
                .filter(user -> user.isActive())
                .filter(userService::canActivateUser)
                .count();

        // Should have some valid users (property-based assertion)
        assertTrue(validUsers > 0, "Should have at least some valid users in bulk generation");
    }

    @Test
    @DisplayName("Custom randomizer demonstration")
    void demonstrateCustomRandomizers() {
        // Create EasyRandom with very specific business constraints
        EasyRandomParameters businessParameters = new EasyRandomParameters()
                .seed(999L)
                // Only generate senior citizens for a senior discount test
                .randomize(FieldPredicates.named("birthDate"),
                    new LocalDateRangeRandomizer(LocalDate.of(1930, 1, 1), LocalDate.of(1958, 12, 31), 111L))
                // Only generate valid email formats
                .randomize(FieldPredicates.named("email"), () -> {
                    String[] domains = {"gmail.com", "yahoo.com", "company.com"};
                    String[] names = {"john", "jane", "alex", "sarah", "mike"};
                    return names[(int)(Math.random() * names.length)] + "@" + domains[(int)(Math.random() * domains.length)];
                })
                // Only generate recent orders
                .randomize(FieldPredicates.named("createdAt"), () ->
                    LocalDateTime.now().minusHours((long)(Math.random() * 48)));

        EasyRandom businessRandom = new EasyRandom(businessParameters);

        // Generate 10 users and verify they all meet our business criteria
        for (int i = 0; i < 10; i++) {
            User user = businessRandom.nextObject(User.class);
            user.setActive(true);

            // All should be seniors due to our birth date range
            assertEquals("SENIOR", userService.getUserCategory(user));

            // All should have valid emails due to our custom randomizer
            assertTrue(userService.isValidEmail(user));

            // All should get senior discount
            assertEquals(0.15, userService.calculateDiscount(user), 0.001);
        }

        // Generate orders and verify they're all recent
        for (int i = 0; i < 5; i++) {
            Order order = businessRandom.nextObject(Order.class);
            assertTrue(orderService.isOrderRecent(order), "Order should be recent: " + order.getCreatedAt());
        }
    }
}
