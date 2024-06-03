package com.example.user.controller;

import com.example.user.model.User;
import com.example.user.security.SecurityConstants;
import com.example.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * REST controller for managing users.
 * <p>
 * This controller handles the user registration process.
 * </p>
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Registers a new user.
	 * <p>
	 * This method checks if the username already exists and validates the user input.
	 * If the user is valid and does not already exist, it encodes the password,
	 * assigns the default user role, and saves the user.
	 * </p>
	 *
	 * @param user the user to register
	 * @return a ResponseEntity with status 201 (Created) if the user is successfully registered,
	 *         409 (Conflict) if the username already exists,
	 *         or 400 (Bad Request) if the user input is invalid
	 */
	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody User user) {
		if (customUserDetailsService.findByUsername(user.getUsername()) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		user.setRoles(Arrays.asList(SecurityConstants.ROLE_USER));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		customUserDetailsService.save(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
