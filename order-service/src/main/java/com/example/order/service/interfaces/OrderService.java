package com.example.order.service.interfaces;

import com.example.order.model.Order;

import java.util.List;
import java.util.Optional;


/**
 * Service interface for managing orders.
 */
public interface OrderService
{

	/**
	 * Saves an order.
	 *
	 * @param order               the order to be saved
	 * @param authorizationHeader the authorization header for accessing product information
	 * @return the saved order
	 * @throws RuntimeException if the product is not available
	 */
	Order save(Order order, String authorizationHeader);

	/**
	 * Retrieves all orders.
	 *
	 * @return a list of all orders
	 */
	List<Order> findAll();

	/**
	 * Finds an order by its ID.
	 *
	 * @param id the ID of the order
	 * @return an Optional containing the order if found, or empty if not found
	 */
	Optional<Order> findById(Long id);

	/**
	 * Deletes an order by its ID.
	 *
	 * @param id the ID of the order to be deleted
	 */
	void deleteById(Long id);

	/**
	 * Finds orders by username.
	 *
	 * @param username the username of the user
	 * @return a list of orders associated with the given username
	 */
	List<Order> findByUserId(String username);
}
