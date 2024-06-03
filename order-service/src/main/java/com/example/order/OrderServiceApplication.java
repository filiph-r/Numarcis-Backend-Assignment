package com.example.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main application class for the Order Service.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {

	/**
	 * Main method to run the Order Service application.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	/**
	 * Creates a LoadBalanced RestTemplate bean.
	 *
	 * @return a RestTemplate instance
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
