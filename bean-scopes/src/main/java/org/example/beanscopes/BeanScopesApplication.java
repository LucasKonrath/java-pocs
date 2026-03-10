package org.example.beanscopes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeanScopesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeanScopesApplication.class, args);
        int i = 0;
    }

}
