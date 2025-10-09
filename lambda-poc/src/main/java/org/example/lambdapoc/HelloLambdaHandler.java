package org.example.lambdapoc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class HelloLambdaHandler implements RequestHandler<String, String> {
    @Override
    public String handleRequest(String input, Context context) {
        String name = (input == null || input.isBlank()) ? "World" : input.trim();
        if (context != null && context.getLogger() != null) {
            context.getLogger().log("Handling request for: " + name + "\n");
        }
        return "Hello, " + name + "!";
    }
}

