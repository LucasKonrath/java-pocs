package org.example.springvalidationpoc.model;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class ValidatedClass {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Min(18)
    @Max(99)
    private int age;

    @Pattern(regexp = "\\d{5}-\\d{3}")
    private String zipCode;

    @URL
    private String website;

    @Past
    private LocalDate birthDate;

    @Future
    private LocalDate appointmentDate;

    @NotNull
    @Size(min = 1)
    private List<@NotBlank String> tags;

    @CreditCardNumber
    private String creditCard;

    @ISBN
    private String isbn;

    @AssertTrue
    private boolean agreedToTerms;

    @AssertFalse
    private boolean isMinor;

    @Positive
    private int positiveNumber;

    @Negative
    private int negativeNumber;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private double ratio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }

    public void setAgreedToTerms(boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
    }

    public boolean isMinor() {
        return isMinor;
    }

    public void setMinor(boolean minor) {
        isMinor = minor;
    }

    public int getPositiveNumber() {
        return positiveNumber;
    }

    public void setPositiveNumber(int positiveNumber) {
        this.positiveNumber = positiveNumber;
    }

    public int getNegativeNumber() {
        return negativeNumber;
    }

    public void setNegativeNumber(int negativeNumber) {
        this.negativeNumber = negativeNumber;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
