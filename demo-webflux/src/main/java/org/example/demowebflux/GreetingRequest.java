package org.example.demowebflux;

import jakarta.validation.constraints.NotBlank;

public class GreetingRequest {
    @NotBlank(message = "name must not be blank")
    private String name;

    public GreetingRequest() {}

    public GreetingRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
