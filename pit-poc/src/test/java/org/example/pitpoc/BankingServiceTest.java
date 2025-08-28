package org.example.pitpoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Banking Service Tests for Mutation Testing POC")
class BankingServiceTest {

    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        bankingService = new BankingService();
    }

    @Test
    @DisplayName("Valid account number should be accepted")
    void testIsValidAccount() {
        assertTrue(bankingService.isValidAccount("12345678"));
        assertTrue(bankingService.isValidAccount("123456789"));
        assertFalse(bankingService.isValidAccount("1234567")); // too short
        assertFalse(bankingService.isValidAccount("12345abc")); // contains letters
        assertFalse(bankingService.isValidAccount(null)); // null
        assertFalse(bankingService.isValidAccount("")); // empty
    }

    @Test
    @DisplayName("Interest calculation should be accurate")
    void testCalculateInterest() {
        assertEquals(100.0, bankingService.calculateInterest(1000, 10, 1), 0.01);
        assertEquals(210.0, bankingService.calculateInterest(1000, 10, 2), 0.01);
        assertEquals(50.0, bankingService.calculateInterest(1000, 5, 1), 0.01);
    }

    @Test
    @DisplayName("Interest calculation with invalid parameters should throw exception")
    void testCalculateInterestInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
            bankingService.calculateInterest(-1000, 10, 1));
        assertThrows(IllegalArgumentException.class, () ->
            bankingService.calculateInterest(1000, -10, 1));
        assertThrows(IllegalArgumentException.class, () ->
            bankingService.calculateInterest(1000, 10, -1));
    }

    @Test
    @DisplayName("Account type determination should be correct")
    void testDetermineAccountType() {
        assertEquals("PREMIUM", bankingService.determineAccountType(150000));
        assertEquals("PREMIUM", bankingService.determineAccountType(100000));
        assertEquals("GOLD", bankingService.determineAccountType(50000));
        assertEquals("GOLD", bankingService.determineAccountType(10000));
        assertEquals("SILVER", bankingService.determineAccountType(5000));
        assertEquals("SILVER", bankingService.determineAccountType(1000));
        assertEquals("BASIC", bankingService.determineAccountType(500));
        assertEquals("BASIC", bankingService.determineAccountType(0));
    }

    @Test
    @DisplayName("Withdrawal eligibility should be determined correctly")
    void testCanWithdraw() {
        assertTrue(bankingService.canWithdraw(1000, 500));
        assertTrue(bankingService.canWithdraw(1000, 1000));
        assertFalse(bankingService.canWithdraw(500, 1000));
        assertFalse(bankingService.canWithdraw(1000, 0));
        assertFalse(bankingService.canWithdraw(1000, -100));
    }

    @Test
    @DisplayName("Withdrawal processing should handle various scenarios")
    void testProcessWithdrawal() {
        assertEquals(500, bankingService.processWithdrawal(1000, 500), 0.01);
        assertEquals(0, bankingService.processWithdrawal(1000, 1000), 0.01);
        assertEquals(-535, bankingService.processWithdrawal(500, 1000), 0.01); // 500 - 1000 - 35 overdraft fee
    }

    @Test
    @DisplayName("Withdrawal with invalid amount should throw exception")
    void testProcessWithdrawalInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () ->
            bankingService.processWithdrawal(1000, 0));
        assertThrows(IllegalArgumentException.class, () ->
            bankingService.processWithdrawal(1000, -100));
    }

    @Test
    @DisplayName("Eligible services should be determined correctly")
    void testGetEligibleServices() {
        List<String> services = bankingService.getEligibleServices(15000, 30);
        assertTrue(services.contains("BASIC_CHECKING"));
        assertTrue(services.contains("SAVINGS_ACCOUNT"));
        assertTrue(services.contains("INVESTMENT_ACCOUNT"));
        assertTrue(services.contains("PREMIUM_SERVICES"));
        assertTrue(services.contains("LOYALTY_REWARDS"));

        services = bankingService.getEligibleServices(2000, 6);
        assertTrue(services.contains("BASIC_CHECKING"));
        assertTrue(services.contains("SAVINGS_ACCOUNT"));
        assertFalse(services.contains("INVESTMENT_ACCOUNT"));
        assertFalse(services.contains("PREMIUM_SERVICES"));
        assertFalse(services.contains("LOYALTY_REWARDS"));

        services = bankingService.getEligibleServices(0, 0);
        assertFalse(services.contains("BASIC_CHECKING"));
    }

    @Test
    @DisplayName("Monthly fee calculation should be correct for all account types")
    void testCalculateMonthlyFee() {
        // Premium accounts
        assertEquals(0.0, bankingService.calculateMonthlyFee("PREMIUM", 50000), 0.01);

        // Gold accounts
        assertEquals(0.0, bankingService.calculateMonthlyFee("GOLD", 6000), 0.01);
        assertEquals(15.0, bankingService.calculateMonthlyFee("GOLD", 3000), 0.01);

        // Silver accounts
        assertEquals(5.0, bankingService.calculateMonthlyFee("SILVER", 2000), 0.01);
        assertEquals(10.0, bankingService.calculateMonthlyFee("SILVER", 500), 0.01);

        // Basic accounts
        assertEquals(10.0, bankingService.calculateMonthlyFee("BASIC", 800), 0.01);
        assertEquals(15.0, bankingService.calculateMonthlyFee("BASIC", 300), 0.01);
    }

    @Test
    @DisplayName("Monthly fee calculation with invalid account type should throw exception")
    void testCalculateMonthlyFeeInvalidType() {
        assertThrows(IllegalArgumentException.class, () ->
            bankingService.calculateMonthlyFee("INVALID", 1000));
    }
}
