package org.example.springboot4poc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @RequestMapping(value = "/test", version = "1")
    public String testV1() {
        return "Hello, Spring Boot 4! V1 Endpoint.";
    }

    @RequestMapping(value = "/test", version = "2")
    public String testV2() {
        return "Hello, Spring Boot 4! V2 Endpoint";
    }

}
