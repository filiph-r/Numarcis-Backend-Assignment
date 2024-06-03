package com.example.user.controller;

import com.example.user.model.User;
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


@RestController
@RequestMapping("/users")
public class UserController
{
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody User user)
	{
		if (customUserDetailsService.findByUsername(user.getUsername()) != null)
		{
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		user.setRoles(Arrays.asList("USER"));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		customUserDetailsService.save(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
