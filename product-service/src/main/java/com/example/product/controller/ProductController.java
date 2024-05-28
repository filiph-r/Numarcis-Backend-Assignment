package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController
{
	@Autowired
	private ProductService productService;

	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product)
	{
		return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts()
	{
		return ResponseEntity.ok(productService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id)
	{
		return productService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product)
	{
		product.setId(id);
		return ResponseEntity.ok(productService.save(product));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id)
	{
		productService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam String query)
	{
		return ResponseEntity.ok(productService.search(query));
	}
}
