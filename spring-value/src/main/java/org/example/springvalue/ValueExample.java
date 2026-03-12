package org.example.springvalue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueExample {

    @Value(("${test.value}"))
    private String value;

    @Value("${doesnt.exist:default value}")
    private String defaultValue;

}
