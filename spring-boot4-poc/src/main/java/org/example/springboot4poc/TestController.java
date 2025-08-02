package org.example.springboot4poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private HttpDogHttpExchange httpDogHttpExchange;

    @RequestMapping(value = "/test", version = "1")
    public String testV1() {
        return "Hello, Spring Boot 4! V1 Endpoint.";
    }

    @RequestMapping(value = "/test", version = "2")
    public String testV2() {
        return "Hello, Spring Boot 4! V2 Endpoint";
    }

    @RequestMapping(value = "/{statusCode}")
    public byte[] getDogImage(@PathVariable int statusCode) {

        return httpDogHttpExchange.getDogImage(statusCode);
    }

}
