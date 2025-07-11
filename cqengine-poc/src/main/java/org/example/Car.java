package org.example;

import lombok.Data;

import java.util.List;

@Data
public class Car {
    Integer id;
    String name;
    String description;
    List<String> optionals;
    String make;
    Integer year;
    Integer hp;
}
