package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



public class ApplicationRunner {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TaxiFareConfiguration.class);
        TaxiFareCalculatorService taxiFareCalculatorService = context.getBean(TaxiFareCalculatorService.class);
        TaxiRide taxiRide = new TaxiRide();
        taxiRide.setNightSurcharge(true);
        taxiRide.setDistanceInMile(190L);
        Fare rideFare = new Fare();
        taxiFareCalculatorService.calculateFare(taxiRide, rideFare);
    }

}