package com.example.order.service;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.repository.OrderRepository;
import com.example.order.security.SecurityConstants;
import com.example.order.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing orders.
 * This class handles the business logic related to orders,
 * including creating, retrieving, and deleting orders.
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Saves an order. Before saving, it validates each product in the order
	 * by calling the product service to check if the product is available.
	 *
	 * @param order the order to save
	 * @param authorizationHeader the authorization header containing the JWT token
	 * @return the saved order
	 * @throws RuntimeException if a product in the order is not available
	 */
	public Order save(Order order, String authorizationHeader) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(SecurityConstants.JWT_HEADER_STRING, authorizationHeader);

		order.getProductIds().forEach(productId -> {
			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<Product> response = restTemplate.exchange(
					SecurityConstants.PRODUCT_SERVICE_URL + productId,
					HttpMethod.GET,
					entity,
					Product.class
			);

			if (response.getStatusCode() != HttpStatus.OK) {
				throw new RuntimeException("Product not available");
			}
		});

		return orderRepository.save(order);
	}

	/**
	 * Retrieves all orders.
	 *
	 * @return a list of all orders
	 */
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	/**
	 * Retrieves an order by its ID.
	 *
	 * @param id the ID of the order
	 * @return an Optional containing the order if found, or empty if not found
	 */
	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	/**
	 * Deletes an order by its ID.
	 *
	 * @param id the ID of the order to delete
	 */
	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}

	/**
	 * Retrieves orders by the username of the user who placed them.
	 *
	 * @param username the username of the user
	 * @return a list of orders placed by the specified user
	 */
	public List<Order> findByUserId(String username) {
		return orderRepository.findByUsername(username);
	}
}
