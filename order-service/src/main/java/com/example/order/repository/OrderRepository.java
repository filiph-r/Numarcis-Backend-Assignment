package com.example.order.repository;

import com.example.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link Order} entities.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 * Finds a list of orders by the username.
	 *
	 * @param username the username of the user whose orders are to be retrieved
	 * @return a list of orders associated with the given username
	 */
	List<Order> findByUsername(String username);
}
