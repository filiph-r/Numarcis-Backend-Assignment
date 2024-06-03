package com.example.order.controller;

import com.example.order.model.Order;
import com.example.order.security.JwtUtil;
import com.example.order.security.SecurityConstants;
import com.example.order.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing orders.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * Creates a new order.
	 *
	 * @param order the order to be created
	 * @param authorizationHeader the authorization header containing the JWT token
	 * @return the created order
	 */
	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order order,
			@RequestHeader(SecurityConstants.JWT_HEADER_STRING) String authorizationHeader) {
		order.setUsername(jwtUtil.getUsername(authorizationHeader.substring(SecurityConstants.JWT_TOKEN_PREFIX.length())));
		return new ResponseEntity<>(orderService.save(order, authorizationHeader), HttpStatus.CREATED);
	}

	/**
	 * Retrieves all orders.
	 *
	 * @param authorizationHeader the authorization header containing the JWT token
	 * @return a list of all orders or orders for the specific user
	 */
	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders(@RequestHeader(SecurityConstants.JWT_HEADER_STRING) String authorizationHeader) {
		if (jwtUtil.getRoles(authorizationHeader.substring(SecurityConstants.JWT_TOKEN_PREFIX.length())).contains(SecurityConstants.ROLE_ADMIN)) {
			return ResponseEntity.ok(orderService.findAll());
		} else {
			return ResponseEntity.ok(orderService.findByUserId(jwtUtil.getUsername(authorizationHeader.substring(SecurityConstants.JWT_TOKEN_PREFIX.length()))));
		}
	}

	/**
	 * Retrieves an order by its ID.
	 *
	 * @param id the ID of the order
	 * @return the order if found, or a 404 Not Found response
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
		return orderService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Updates an existing order.
	 *
	 * @param id the ID of the order to be updated
	 * @param order the updated order details
	 * @param authorizationHeader the authorization header containing the JWT token
	 * @return the updated order
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable("id") Long id, @RequestBody Order order,
			@RequestHeader(SecurityConstants.JWT_HEADER_STRING) String authorizationHeader) {
		order.setId(id);
		order.setUsername(jwtUtil.getUsername(authorizationHeader.substring(SecurityConstants.JWT_TOKEN_PREFIX.length())));
		return ResponseEntity.ok(orderService.save(order, authorizationHeader));
	}

	/**
	 * Deletes an order by its ID.
	 *
	 * @param id the ID of the order to be deleted
	 * @return a 204 No Content response
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
		orderService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Retrieves orders for a specific user by their username.
	 *
	 * @param username the username of the user
	 * @return a list of orders for the specified user
	 */
	@GetMapping("/user/{username}")
	public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable("username") String username) {
		return ResponseEntity.ok(orderService.findByUserId(username));
	}
}
