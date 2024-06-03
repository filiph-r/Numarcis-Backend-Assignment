package com.example.product.repository;

import com.example.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Product} entities.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * Finds products by name, description, or category containing the specified keyword.
	 *
	 * @param name the name to search for
	 * @param description the description to search for
	 * @param category the category to search for
	 * @return a list of products matching the search criteria
	 */
	List<Product> findByNameContainingOrDescriptionContainingOrCategoryContaining(String name, String description, String category);
}
