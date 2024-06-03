package com.example.product.service.interfaces;

import com.example.product.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing products.
 */
public interface ProductService {

	/**
	 * Saves a product.
	 *
	 * @param product the product to save
	 * @return the saved product
	 */
	Product save(Product product);

	/**
	 * Retrieves all products.
	 *
	 * @return a list of all products
	 */
	List<Product> findAll();

	/**
	 * Retrieves a product by its ID.
	 *
	 * @param id the ID of the product to retrieve
	 * @return an Optional containing the found product, or an empty Optional if no product was found
	 */
	Optional<Product> findById(Long id);

	/**
	 * Deletes a product by its ID.
	 *
	 * @param id the ID of the product to delete
	 */
	void deleteById(Long id);

	/**
	 * Searches for products by a query string.
	 *
	 * @param query the query string to search for
	 * @return a list of products matching the search criteria
	 */
	List<Product> search(String query);
}
