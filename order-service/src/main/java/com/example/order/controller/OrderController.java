package com.example.order.controller;

import com.example.order.model.Order;
import com.example.order.security.JwtUtil;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController
{
	@Autowired
	private OrderService orderService;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order order, @RequestHeader("Authorization") String authorizationHeader)
	{
		order.setUsername(jwtUtil.getUsername(authorizationHeader.substring(7)));
		return new ResponseEntity<>(orderService.save(order, authorizationHeader), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String authorizationHeader)
	{
		if (jwtUtil.getRoles(authorizationHeader.substring(7)).contains("ADMIN"))
		{
			return ResponseEntity.ok(orderService.findAll());
		}
		else
		{
			return ResponseEntity.ok(orderService.findByUserId(jwtUtil.getUsername(authorizationHeader.substring(7))));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id)
	{
		return orderService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable("id") Long id, @RequestBody Order order,
			@RequestHeader("Authorization") String authorizationHeader)
	{
		order.setId(id);
		order.setUsername(jwtUtil.getUsername(authorizationHeader.substring(7)));
		return ResponseEntity.ok(orderService.save(order, authorizationHeader));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id)
	{
		orderService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/user/{username}")
	public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable("username") String username)
	{
		return ResponseEntity.ok(orderService.findByUserId(username));
	}
}
