package com.aj.foodapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodappApplication {

	public static void main(String[] args) {

		SpringApplication.run(FoodappApplication.class, args);

		Logger log = LoggerFactory.getLogger(FoodappApplication.class);

		log.info("Hello AJ Tech -> Food App Started Successfully");

	}


}
