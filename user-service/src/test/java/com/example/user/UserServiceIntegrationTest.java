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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserServiceIntegrationTest
{

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	void setUp()
	{
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	void testUserRegistration() throws Exception
	{
		User user = new User();
		user.setUsername("testuser1");
		user.setPassword("password");

		mockMvc.perform(post("/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isCreated());
	}

	@Test
	void testUserRegistrationWithMissingData() throws Exception
	{
		User user = new User();
		user.setUsername("");
		user.setPassword("password");

		mockMvc.perform(post("/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testUserRegistrationWithInvalidData() throws Exception
	{
		User user = new User();
		user.setUsername("testuser");
		user.setPassword(""); // Invalid password

		mockMvc.perform(post("/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testUserLogin() throws Exception
	{
		// First register a user
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

	@Test
	void testUserLoginWithInvalidCredentials() throws Exception
	{
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("nonexistentuser");
		authenticationRequest.setPassword("wrongpassword");

		mockMvc.perform(post("/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(authenticationRequest)))
				.andExpect(status().isUnauthorized());
	}

	private static String asJsonString(final Object obj)
	{
		try
		{
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
