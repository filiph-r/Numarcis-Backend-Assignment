package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing products.
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Saves a product.
	 *
	 * @param product the product to save
	 * @return the saved product
	 */
	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	/**
	 * Retrieves all products.
	 *
	 * @return a list of all products
	 */
	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	/**
	 * Retrieves a product by its ID.
	 *
	 * @param id the ID of the product to retrieve
	 * @return an Optional containing the found product, or an empty Optional if no product was found
	 */
	@Override
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	/**
	 * Deletes a product by its ID.
	 *
	 * @param id the ID of the product to delete
	 */
	@Override
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	/**
	 * Searches for products by a query string.
	 *
	 * @param query the query string to search for
	 * @return a list of products matching the search criteria
	 */
	@Override
	public List<Product> search(String query) {
		return productRepository.findByNameContainingOrDescriptionContainingOrCategoryContaining(query, query, query);
	}
}
