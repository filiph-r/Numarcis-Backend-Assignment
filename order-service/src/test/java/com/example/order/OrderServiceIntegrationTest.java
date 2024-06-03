package com.example.order;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.repository.OrderRepository;
import com.example.order.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceIntegrationTest
{

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@MockBean
	private RestTemplate restTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();

	private Order order;



	@BeforeEach
	public void setUp()
	{
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		orderRepository.deleteAll();
		order = new Order();
		order.setUsername("Tester");
		order.setProductIds(Collections.singletonList(1l));
		order = orderRepository.save(order);
	}

	@Test
	public void createOrder() throws Exception
	{
		Order newOrder = new Order();
		newOrder.setProductIds(Collections.singletonList(1l));

		HttpHeaders headers = new HttpHeaders();
		String headerValue = "Bearer " + jwtUtil.createToken("username", Collections.singletonList("ADMIN"));
		headers.add("Authorization", headerValue);

		ResponseEntity<Product> response = new ResponseEntity<>(HttpStatus.OK);
		when(restTemplate.exchange(
				"http://PRODUCT-SERVICE/products/" + 1l,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				Product.class
		)).thenReturn(response);

		mockMvc.perform(post("/orders")
						.headers(headers)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newOrder)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.username", is("username")))
				.andExpect(jsonPath("$.productIds", is(Arrays.asList(1))));
	}

	@Test
	public void getOrderById() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		String headerValue = "Bearer " + jwtUtil.createToken("admin", Collections.singletonList("ADMIN"));
		headers.add("Authorization", headerValue);

		mockMvc.perform(get("/orders/{id}", order.getId())
						.headers(headers))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.username", is(order.getUsername())))
				.andExpect(jsonPath("$.productIds", is(Arrays.asList(1))));

		headers = new HttpHeaders();
		headerValue = "Bearer " + jwtUtil.createToken("Tester", Collections.singletonList("USER"));
		headers.add("Authorization", headerValue);

		mockMvc.perform(get("/orders/{id}", order.getId())
						.headers(headers))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.username", is(order.getUsername())))
				.andExpect(jsonPath("$.productIds", is(Arrays.asList(1))));

		headers = new HttpHeaders();
		headerValue = "Bearer " + jwtUtil.createToken("WrongUsername", Collections.singletonList("USER"));
		headers.add("Authorization", headerValue);

		mockMvc.perform(get("/orders/{id}", order.getId())
						.headers(headers))
				.andExpect(status().isForbidden());
	}

	@Test
	public void updateOrder() throws Exception
	{
		order.setProductIds(Collections.singletonList(1l));

		HttpHeaders headers = new HttpHeaders();
		String headerValue = "Bearer " + jwtUtil.createToken("admin", Collections.singletonList("ADMIN"));
		headers.add("Authorization", headerValue);

		ResponseEntity<Product> response = new ResponseEntity<>(HttpStatus.OK);
		when(restTemplate.exchange(
				"http://PRODUCT-SERVICE/products/" + 1l,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				Product.class
		)).thenReturn(response);

		mockMvc.perform(put("/orders/{id}", order.getId())
						.headers(headers)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.username", is("admin")))
				.andExpect(jsonPath("$.productIds", is(Arrays.asList(1))));
	}

	@Test
	public void deleteOrder() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		String headerValue = "Bearer " + jwtUtil.createToken("admin", Collections.singletonList("ADMIN"));
		headers.add("Authorization", headerValue);

		mockMvc.perform(delete("/orders/{id}", order.getId())
						.headers(headers))
				.andExpect(status().isNoContent());

		Optional<Order> deletedOrder = orderRepository.findById(order.getId());
		assertTrue(deletedOrder.isEmpty());
	}

	@Test
	public void getOrdersByUserId() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		String headerValue = "Bearer " + jwtUtil.createToken("admin", Collections.singletonList("ADMIN"));
		headers.add("Authorization", headerValue);

		mockMvc.perform(get("/orders/user/{userId}", order.getUsername())
						.headers(headers))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(order.getId().intValue())))
				.andExpect(jsonPath("$[0].username", is(order.getUsername())))
				.andExpect(jsonPath("$[0].productIds[0]", is(order.getProductIds().get(0).intValue())));
	}
}
