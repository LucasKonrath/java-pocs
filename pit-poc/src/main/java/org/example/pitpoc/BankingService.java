package org.example.pitpoc;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

/**
 * Banking service class to demonstrate complex mutation testing scenarios
 */
@Service
public class BankingService {

    private static final double MINIMUM_BALANCE = 0.0;
    private static final double OVERDRAFT_FEE = 35.0;

    public boolean isValidAccount(String accountNumber) {
        return accountNumber != null && accountNumber.length() >= 8 && accountNumber.matches("\\d+");
    }

    public double calculateInterest(double principal, double rate, int years) {
        if (principal <= 0 || rate < 0 || years <= 0) {
            throw new IllegalArgumentException("Invalid parameters for interest calculation");
        }
        return principal * Math.pow(1 + rate / 100, years) - principal;
    }

    public String determineAccountType(double balance) {
        if (balance >= 100000) {
            return "PREMIUM";
        } else if (balance >= 10000) {
            return "GOLD";
        } else if (balance >= 1000) {
            return "SILVER";
        } else {
            return "BASIC";
        }
    }

    public boolean canWithdraw(double currentBalance, double withdrawAmount) {
        return currentBalance >= withdrawAmount && withdrawAmount > 0;
    }

    public double processWithdrawal(double currentBalance, double withdrawAmount) {
        if (withdrawAmount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (currentBalance >= withdrawAmount) {
            return currentBalance - withdrawAmount;
        } else {
            // Overdraft scenario
            return currentBalance - withdrawAmount - OVERDRAFT_FEE;
        }
    }

    public List<String> getEligibleServices(double balance, int accountAge) {
        List<String> services = new ArrayList<>();

        if (balance > 0) {
            services.add("BASIC_CHECKING");
        }

        if (balance >= 1000) {
            services.add("SAVINGS_ACCOUNT");
        }

        if (balance >= 5000 && accountAge >= 12) {
            services.add("INVESTMENT_ACCOUNT");
        }

        if (balance >= 10000) {
            services.add("PREMIUM_SERVICES");
        }

        if (accountAge >= 24) {
            services.add("LOYALTY_REWARDS");
        }

        return services;
    }

    public double calculateMonthlyFee(String accountType, double balance) {
        switch (accountType.toUpperCase()) {
            case "PREMIUM":
                return 0.0; // No fees for premium accounts
            case "GOLD":
                return balance >= 5000 ? 0.0 : 15.0;
            case "SILVER":
                return balance >= 1000 ? 5.0 : 10.0;
            case "BASIC":
                return balance >= 500 ? 10.0 : 15.0;
            default:
                throw new IllegalArgumentException("Unknown account type: " + accountType);
        }
    }
}
