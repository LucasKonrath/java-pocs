package org.example.lambdapoc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloLambdaHandlerTest {

    @Test
    void returnsGreetingForProvidedName() {
        HelloLambdaHandler handler = new HelloLambdaHandler();
        String result = handler.handleRequest("Alice", null);
        assertEquals("Hello, Alice!", result);
    }

    @Test
    void returnsGreetingForWorldWhenNoName() {
        HelloLambdaHandler handler = new HelloLambdaHandler();
        String result = handler.handleRequest("", null);
        assertEquals("Hello, World!", result);
    }
}

