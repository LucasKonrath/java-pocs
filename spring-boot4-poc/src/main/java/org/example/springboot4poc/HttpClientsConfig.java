package org.example.springboot4poc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(HttpDogHttpExchange.class)
public class HttpClientsConfig {
}
