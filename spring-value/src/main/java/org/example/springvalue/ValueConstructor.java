package org.example.springvalue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueConstructor {

    private final String testValue;

    public ValueConstructor(@Value("${test.value}") String testValue) {
        this.testValue = testValue;
    }
}
