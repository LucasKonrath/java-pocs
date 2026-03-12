package org.example.beanpostprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeanPostProcessorApplication implements CommandLineRunner {

	@Autowired
	private TestBean testBean;

	public static void main(String[] args) {
		SpringApplication.run(BeanPostProcessorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
