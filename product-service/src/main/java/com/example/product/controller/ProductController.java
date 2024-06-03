package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing products.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * Creates a new product.
	 *
	 * @param product the product to create
	 * @return the created product with HTTP status 201 (Created)
	 */
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
	}

	/**
	 * Retrieves all products.
	 *
	 * @return a list of all products with HTTP status 200 (OK)
	 */
	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.ok(productService.findAll());
	}

	/**
	 * Retrieves a product by its ID.
	 *
	 * @param id the ID of the product to retrieve
	 * @return the requested product with HTTP status 200 (OK), or 404 (Not Found) if the product does not exist
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
		return productService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Updates an existing product.
	 *
	 * @param id the ID of the product to update
	 * @param product the updated product details
	 * @return the updated product with HTTP status 200 (OK)
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
		product.setId(id);
		return ResponseEntity.ok(productService.save(product));
	}

	/**
	 * Deletes a product by its ID.
	 *
	 * @param id the ID of the product to delete
	 * @return HTTP status 204 (No Content)
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
		productService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Searches for products by a query string.
	 *
	 * @param query the query string to search for
	 * @return a list of products matching the query with HTTP status 200 (OK)
	 */
	@GetMapping("/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam("query") String query) {
		return ResponseEntity.ok(productService.search(query));
	}
}
