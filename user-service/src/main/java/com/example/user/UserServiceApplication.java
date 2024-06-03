package com.example.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the User Service.
 * <p>
 * This class contains the main method that starts the Spring Boot application. It is also annotated with
 * {@link EnableDiscoveryClient} to enable service discovery for microservices.
 * </p>
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
