package org.example.springvalidationpoc.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatedClassTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private ValidatedClass createValidInstance() {
        ValidatedClass obj = new ValidatedClass();
        obj.setName("John Doe");
        obj.setEmail("john@example.com");
        obj.setAge(30);
        obj.setZipCode("12345-678");
        obj.setWebsite("https://example.com");
        obj.setBirthDate(LocalDate.of(1990, 1, 1));
        obj.setAppointmentDate(LocalDate.now().plusDays(1));
        obj.setTags(Arrays.asList("tag1"));
        obj.setCreditCard("4111111111111111");
        obj.setIsbn("978-3-16-148410-0");
        obj.setAgreedToTerms(true);
        obj.setMinor(false);
        obj.setNegativeNumber(-1);
        obj.setPositiveNumber(1);
        return obj;
    }

    @Test
    public void testValidInstance() {
        ValidatedClass obj = createValidInstance();
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), "There should be no violations for a valid instance");
    }

    @Test
    public void testBlankName() {
        ValidatedClass obj = createValidInstance();
        obj.setName("");
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testInvalidEmail() {
        ValidatedClass obj = createValidInstance();
        obj.setEmail("not-an-email");
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testAgeTooLow() {
        ValidatedClass obj = createValidInstance();
        obj.setAge(10);
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("age")));
    }

    @Test
    public void testAgeTooHigh() {
        ValidatedClass obj = createValidInstance();
        obj.setAge(120);
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("age")));
    }

    @Test
    public void testInvalidZipCode() {
        ValidatedClass obj = createValidInstance();
        obj.setZipCode("1234");
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("zipCode")));
    }

    @Test
    public void testInvalidWebsite() {
        ValidatedClass obj = createValidInstance();
        obj.setWebsite("not-a-url");
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("website")));
    }

    @Test
    public void testPastBirthDate() {
        ValidatedClass obj = createValidInstance();
        obj.setBirthDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthDate")));
    }

    @Test
    public void testFutureAppointmentDate() {
        ValidatedClass obj = createValidInstance();
        obj.setAppointmentDate(LocalDate.now().minusDays(1));
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("appointmentDate")));
    }

    @Test
    public void testEmptyTags() {
        ValidatedClass obj = createValidInstance();
        obj.setTags(new ArrayList<>());
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tags")));
    }

    @Test
    public void testBlankTag() {
        ValidatedClass obj = createValidInstance();
        obj.setTags(Arrays.asList(""));
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().contains("tags")));
    }

    @Test
    public void testInvalidCreditCard() {
        ValidatedClass obj = createValidInstance();
        obj.setCreditCard("1234567890123456");
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("creditCard")));
    }

    @Test
    public void testInvalidIsbn() {
        ValidatedClass obj = createValidInstance();
        obj.setIsbn("invalid-isbn");
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("isbn")));
    }

    @Test
    public void testAssertTrueActive() {
        ValidatedClass obj = createValidInstance();
        obj.setAgreedToTerms(false);
        Set<ConstraintViolation<ValidatedClass>> violations = validator.validate(obj);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("agreedToTerms")));
    }
}

