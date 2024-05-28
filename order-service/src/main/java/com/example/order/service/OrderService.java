package com.example.order.service;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestTemplate restTemplate;

	public Order save(Order order) {
		// Check product availability
		order.getProductIds().forEach(productId -> {
			ResponseEntity<Product> response = restTemplate.getForEntity("http://PRODUCT-SERVICE/products/" + productId, Product.class);
			if (response.getStatusCode() != HttpStatus.OK) {
				throw new RuntimeException("Product not available");
			}
		});

		return orderRepository.save(order);
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}

	public List<Order> findByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}
}
