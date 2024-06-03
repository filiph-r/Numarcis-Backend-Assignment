package com.example.product;

import com.example.product.model.Product;
import com.example.product.security.JwtUtil;
import com.example.product.security.SecurityConstants;
import com.example.product.service.interfaces.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the ProductService.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductServiceIntegrationTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private JwtUtil jwtUtil;

	@MockBean
	private ProductService productService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private Product product;

	/**
	 * Sets up the test environment before each test.
	 */
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setDescription("Test Description");
		product.setCategory("Test Category");
	}

	/**
	 * Tests the creation of a product.
	 */
	@Test
	void testCreateProduct() throws Exception {
		when(productService.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

		HttpHeaders headers = new HttpHeaders();
		headers.add(SecurityConstants.JWT_HEADER_STRING, SecurityConstants.JWT_TOKEN_PREFIX + jwtUtil.createToken("username",
				Collections.singletonList(SecurityConstants.ROLE_ADMIN)));

		mockMvc.perform(post("/products")
						.headers(headers)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(product)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(product.getId().intValue())))
				.andExpect(jsonPath("$.name", is(product.getName())))
				.andExpect(jsonPath("$.description", is(product.getDescription())))
				.andExpect(jsonPath("$.category", is(product.getCategory())));
	}

	/**
	 * Tests retrieving all products.
	 */
	@Test
	void testGetAllProducts() throws Exception {
		Product product1 = new Product();
		product1.setId(2L);
		product1.setName("Test Product 1");
		product1.setDescription("Test Description 1");
		product1.setCategory("Test Category 1");

		Product product2 = new Product();
		product2.setId(3L);
		product2.setName("Test Product 2");
		product2.setDescription("Test Description 2");
		product2.setCategory("Test Category 2");

		List<Product> products = Arrays.asList(product1, product2);

		when(productService.findAll()).thenReturn(products);

		HttpHeaders headers = new HttpHeaders();
		headers.add(SecurityConstants.JWT_HEADER_STRING, SecurityConstants.JWT_TOKEN_PREFIX + jwtUtil.createToken("username",
				Collections.singletonList(SecurityConstants.ROLE_ADMIN)));

		mockMvc.perform(get("/products")
						.headers(headers))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", notNullValue()))
				.andExpect(jsonPath("$[0].name", is(product1.getName())))
				.andExpect(jsonPath("$[0].description", is(product1.getDescription())))
				.andExpect(jsonPath("$[0].category", is(product1.getCategory())))
				.andExpect(jsonPath("$[1].id", notNullValue()))
				.andExpect(jsonPath("$[1].name", is(product2.getName())))
				.andExpect(jsonPath("$[1].description", is(product2.getDescription())))
				.andExpect(jsonPath("$[1].category", is(product2.getCategory())));
	}

	/**
	 * Tests retrieving a product by its ID.
	 */
	@Test
	void testGetProductById() throws Exception {
		Product product = new Product();
		product.setId(4L);
		product.setName("Test Product");
		product.setDescription("Test Description");
		product.setCategory("Test Category");

		when(productService.findById(4L)).thenReturn(Optional.of(product));

		HttpHeaders headers = new HttpHeaders();
		headers.add(SecurityConstants.JWT_HEADER_STRING, SecurityConstants.JWT_TOKEN_PREFIX + jwtUtil.createToken("username",
				Collections.singletonList(SecurityConstants.ROLE_ADMIN)));

		mockMvc.perform(get("/products/{id}", product.getId())
						.headers(headers))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(product.getId().intValue())))
				.andExpect(jsonPath("$.name", is(product.getName())))
				.andExpect(jsonPath("$.description", is(product.getDescription())))
				.andExpect(jsonPath("$.category", is(product.getCategory())));
	}

	/**
	 * Tests updating a product.
	 */
	@Test
	void testUpdateProduct() throws Exception {
		Product product = new Product();
		product.setId(5L);
		product.setName("Test Product");
		product.setDescription("Test Description");
		product.setCategory("Test Category");

		Product updatedProduct = new Product();
		updatedProduct.setId(5L);
		updatedProduct.setName("Updated Test Product");
		updatedProduct.setDescription("Updated Test Description");
		updatedProduct.setCategory("Updated Test Category");

		when(productService.findById(5L)).thenReturn(Optional.of(product));
		when(productService.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

		HttpHeaders headers = new HttpHeaders();
		headers.add(SecurityConstants.JWT_HEADER_STRING, SecurityConstants.JWT_TOKEN_PREFIX + jwtUtil.createToken("username",
				Collections.singletonList(SecurityConstants.ROLE_ADMIN)));

		mockMvc.perform(put("/products/{id}", product.getId())
						.headers(headers)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedProduct)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(updatedProduct.getId().intValue())))
				.andExpect(jsonPath("$.name", is(updatedProduct.getName())))
				.andExpect(jsonPath("$.description", is(updatedProduct.getDescription())))
				.andExpect(jsonPath("$.category", is(updatedProduct.getCategory())));
	}

	/**
	 * Tests deleting a product.
	 */
	@Test
	void testDeleteProduct() throws Exception {
		Mockito.doNothing().when(productService).deleteById(anyLong());

		HttpHeaders headers = new HttpHeaders();
		headers.add(SecurityConstants.JWT_HEADER_STRING, SecurityConstants.JWT_TOKEN_PREFIX + jwtUtil.createToken("username",
				Collections.singletonList(SecurityConstants.ROLE_ADMIN)));

		mockMvc.perform(delete("/products/{id}", product.getId())
						.headers(headers))
				.andExpect(status().isNoContent());
	}

	/**
	 * Tests searching for products.
	 */
	@Test
	void testSearchProducts() throws Exception {
		when(productService.search(any(String.class))).thenReturn(Collections.singletonList(product));

		HttpHeaders headers = new HttpHeaders();
		headers.add(SecurityConstants.JWT_HEADER_STRING, SecurityConstants.JWT_TOKEN_PREFIX + jwtUtil.createToken("username",
				Collections.singletonList(SecurityConstants.ROLE_ADMIN)));

		mockMvc.perform(get("/products/search").param("query", "Test").headers(headers))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(product.getId().intValue())))
				.andExpect(jsonPath("$[0].name", is(product.getName())))
				.andExpect(jsonPath("$[0].description", is(product.getDescription())))
				.andExpect(jsonPath("$[0].category", is(product.getCategory())));
	}
}
