package com.example.user;

import com.example.user.model.User;
import com.example.user.security.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the User Service.
 * This class tests the registration and login functionalities for users.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserServiceIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Sets up the MockMvc instance with Spring Security before each test.
	 */
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	/**
	 * Tests the user registration with valid data.
	 * Expects HTTP status 201 (Created) on successful registration.
	 *
	 * @throws Exception if an error occurs during the request
	 */
	@Test
	void testUserRegistration() throws Exception {
		User user = new User();
		user.setUsername("testuser1");
		user.setPassword("password");

		mockMvc.perform(post("/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isCreated());
	}

	/**
	 * Tests the user registration with missing data.
	 * Expects HTTP status 400 (Bad Request) when username is missing.
	 *
	 * @throws Exception if an error occurs during the request
	 */
	@Test
	void testUserRegistrationWithMissingData() throws Exception {
		User user = new User();
		user.setUsername("");
		user.setPassword("password");

		mockMvc.perform(post("/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Tests the user registration with invalid data.
	 * Expects HTTP status 400 (Bad Request) when password is invalid.
	 *
	 * @throws Exception if an error occurs during the request
	 */
	@Test
	void testUserRegistrationWithInvalidData() throws Exception {
		User user = new User();
		user.setUsername("testuser");
		user.setPassword(""); // Invalid password

		mockMvc.perform(post("/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Tests the user login with valid credentials.
	 * Expects HTTP status 200 (OK) on successful login.
	 *
	 * @throws Exception if an error occurs during the request
	 */
	@Test
	void testUserLogin() throws Exception {
		User user = new User();
		user.setUsername("testuser2");
		user.setPassword("password");

		mockMvc.perform(post("/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isCreated());

		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("testuser2");
		authenticationRequest.setPassword("password");

		mockMvc.perform(post("/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(authenticationRequest)))
				.andExpect(status().isOk());
	}

	/**
	 * Tests the user login with invalid credentials.
	 * Expects HTTP status 401 (Unauthorized) when credentials are invalid.
	 *
	 * @throws Exception if an error occurs during the request
	 */
	@Test
	void testUserLoginWithInvalidCredentials() throws Exception {
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("nonexistentuser");
		authenticationRequest.setPassword("wrongpassword");

		mockMvc.perform(post("/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(authenticationRequest)))
				.andExpect(status().isUnauthorized());
	}

	/**
	 * Converts an object to its JSON representation as a string.
	 *
	 * @param obj the object to convert
	 * @return the JSON string representation of the object
	 * @throws RuntimeException if an error occurs during conversion
	 */
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
