package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService
{
	@Autowired
	private ProductRepository productRepository;

	public Product save(Product product)
	{
		return productRepository.save(product);
	}

	public List<Product> findAll()
	{
		return productRepository.findAll();
	}

	public Optional<Product> findById(Long id)
	{
		return productRepository.findById(id);
	}

	public void deleteById(Long id)
	{
		productRepository.deleteById(id);
	}

	public List<Product> search(String query)
	{
		return productRepository.findByNameContainingOrDescriptionContainingOrCategoryContaining(query, query, query);
	}
}
