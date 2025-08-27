package org.example.easyrandompoc.service;

import org.example.easyrandompoc.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {

    public boolean isAdult(User user) {
        if (user == null || user.getBirthDate() == null) {
            return false;
        }
        int age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        return age >= 18;
    }

    public boolean isValidEmail(User user) {
        if (user == null || user.getEmail() == null) {
            return false;
        }
        String email = user.getEmail();
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }

    public boolean canActivateUser(User user) {
        if (user == null) {
            return false;
        }
        return isAdult(user) && isValidEmail(user) && user.getName() != null && !user.getName().trim().isEmpty();
    }

    public String getUserCategory(User user) {
        if (user == null || user.getBirthDate() == null) {
            return "UNKNOWN";
        }

        int age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();

        if (age < 18) {
            return "MINOR";
        } else if (age >= 18 && age < 65) {
            return "ADULT";
        } else {
            return "SENIOR";
        }
    }

    public double calculateDiscount(User user) {
        if (user == null || !user.isActive()) {
            return 0.0;
        }

        String category = getUserCategory(user);
        switch (category) {
            case "SENIOR":
                return 0.15; // 15% discount for seniors
            case "ADULT":
                return 0.05; // 5% discount for adults
            case "MINOR":
                return 0.10; // 10% discount for minors
            default:
                return 0.0;
        }
    }
}
