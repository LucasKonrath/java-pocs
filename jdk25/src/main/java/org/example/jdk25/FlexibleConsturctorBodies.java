package org.example.jdk25;

public class FlexibleConsturctorBodies {

    class Person {
        int age;
        Person(int age) {
            this.age = age;
        }
    }

    class Adult extends Person {
        Adult(int age) {
            if (age < 18) {
                throw new IllegalArgumentException("Age must be 18 or older");
            }
            super(age);
        }
    }
}
