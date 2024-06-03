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


@Service
public class OrderServiceImpl implements OrderService
{
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestTemplate restTemplate;

	public Order save(Order order, String authorizationHeader)
	{
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

			if (response.getStatusCode() != HttpStatus.OK)
			{
				throw new RuntimeException("Product not available");
			}
		});

		return orderRepository.save(order);
	}

	public List<Order> findAll()
	{
		return orderRepository.findAll();
	}

	public Optional<Order> findById(Long id)
	{
		return orderRepository.findById(id);
	}

	public void deleteById(Long id)
	{
		orderRepository.deleteById(id);
	}

	public List<Order> findByUserId(String username)
	{
		return orderRepository.findByUsername(username);
	}
}
