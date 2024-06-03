package com.example.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main class for the Product Service application.
 * This class bootstraps the application using Spring Boot.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApplication {

	/**
	 * The main method, which is the entry point of the application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
}
