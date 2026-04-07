package org.example.gsonpoc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.List;

public class Employee extends Person {

    @SerializedName("company_name")
    @Expose
    private String company;

    @Expose
    private Address address;

    @Expose(serialize = false)
    private double salary;

    public Employee() {
    }

    public Employee(String name, int age, String email, List<String> hobbies,
                    LocalDate birthDate, String company, Address address, double salary) {
        super(name, age, email, hobbies, birthDate);
        this.company = company;
        this.address = address;
        this.salary = salary;
    }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public String toString() {
        return "Employee{company='%s', address=%s, salary=%.2f, %s}"
                .formatted(company, address, salary, super.toString());
    }
}
