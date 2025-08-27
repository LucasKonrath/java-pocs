package org.example.easyrandompoc.model;

import java.time.LocalDate;

public class User {
    private String name;
    private String email;
    private int age;
    private LocalDate birthDate;
    private boolean active;

    public User() {}

    public User(String name, String email, int age, LocalDate birthDate, boolean active) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.birthDate = birthDate;
        this.active = active;
    }

    // Getters and Setters
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", birthDate=" + birthDate +
                ", active=" + active +
                '}';
    }
}
