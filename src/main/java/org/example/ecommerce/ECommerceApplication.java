package org.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableScheduling
@EnableAsync
public class 	ECommerceApplication {

	public static void main(String[] args) {

		SpringApplication.run(ECommerceApplication.class, args);

	}


}
