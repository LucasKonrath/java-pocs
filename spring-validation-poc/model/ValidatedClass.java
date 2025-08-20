package org.example.springvalidationpoc.model;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class ValidatedClass {
    // Not null and not empty string
    @NotBlank
    private String name;

    // Must be a valid email
    @Email
    private String email;

    // Must be between 18 and 99
    @Min(18)
    @Max(99)
    private int age;

    // Must match a regex pattern
    @Pattern(regexp = "\\d{5}-\\d{3}")
    private String zipCode;

    // Must be a valid URL
    @URL
    private String website;

    // Must be a past date
    @Past
    private LocalDate birthDate;

    // Must be a future date
    @Future
    private LocalDate appointmentDate;

    // Must not be null and must have at least 1 element
    @NotNull
    @Size(min = 1)
    private List<@NotBlank String> tags;

    // Must be a valid credit card number
    @CreditCardNumber
    private String creditCard;

    // Must be a valid ISBN
    @ISBN
    private String isbn;

    // Must be true
    @AssertTrue
    private boolean agreedToTerms;

    // Must be false
    @AssertFalse
    private boolean isMinor;

    // Must be a positive number
    @Positive
    private int positiveNumber;

    // Must be a negative number
    @Negative
    private int negativeNumber;

    // Must be a decimal between 0.0 and 1.0
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private double ratio;

    // ...getters and setters...
}
