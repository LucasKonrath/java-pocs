package org.example.springbatchpoc.processor;

import org.example.springbatchpoc.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {

    private static final Logger log = LoggerFactory.getLogger(EmployeeItemProcessor.class);

    @Override
    public Employee process(Employee employee) {
        // Apply business logic transformations

        // 1. Normalize names to uppercase
        String firstName = employee.getFirstName().toUpperCase();
        String lastName = employee.getLastName().toUpperCase();

        // 2. Ensure email is lowercase
        String email = employee.getEmail().toLowerCase();

        // 3. Apply salary bonus based on department
        Double salary = employee.getSalary();
        String department = employee.getDepartment();

        if ("ENGINEERING".equalsIgnoreCase(department)) {
            salary = salary * 1.10; // 10% bonus for engineering
        } else if ("SALES".equalsIgnoreCase(department)) {
            salary = salary * 1.05; // 5% bonus for sales
        }

        Employee transformedEmployee = new Employee(firstName, lastName, email, department.toUpperCase(), salary);

        log.info("Converting ({}) into ({})", employee, transformedEmployee);

        return transformedEmployee;
    }
}
