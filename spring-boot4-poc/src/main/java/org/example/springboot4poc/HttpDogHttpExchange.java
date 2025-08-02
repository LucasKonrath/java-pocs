package org.example.springboot4poc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("https://http.dog")
public interface HttpDogHttpExchange {

    @GetExchange("/{statusCode}.jpg")
    byte[] getDogImage(@PathVariable int statusCode);

}
