package com.example.order.controller;

import com.example.order.model.Order;
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

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order order)
	{
		return new ResponseEntity<>(orderService.save(order), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders()
	{
		return ResponseEntity.ok(orderService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id)
	{
		return orderService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order)
	{
		order.setId(id);
		return ResponseEntity.ok(orderService.save(order));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id)
	{
		orderService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId)
	{
		return ResponseEntity.ok(orderService.findByUserId(userId));
	}
}
