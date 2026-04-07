package org.example.gsonpoc.model;

import java.time.LocalDate;
import java.util.List;

public class Person {

    private String name;
    private int age;
    private String email;
    private List<String> hobbies;
    private LocalDate birthDate;

    public Person() {
    }

    public Person(String name, int age, String email, List<String> hobbies, LocalDate birthDate) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.hobbies = hobbies;
        this.birthDate = birthDate;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<String> getHobbies() { return hobbies; }
    public void setHobbies(List<String> hobbies) { this.hobbies = hobbies; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    @Override
    public String toString() {
        return "Person{name='%s', age=%d, email='%s', hobbies=%s, birthDate=%s}"
                .formatted(name, age, email, hobbies, birthDate);
    }
}
