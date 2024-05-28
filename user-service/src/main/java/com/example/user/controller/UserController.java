package com.example.user.controller;

import com.example.user.model.User;
import com.example.user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		user.setRole("USER");
		return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		User foundUser = userService.findByUsername(user.getUsername());
		if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
			String token = Jwts.builder().setSubject(foundUser.getUsername())
					.claim("roles", foundUser.getRole()).signWith(SignatureAlgorithm.HS256, "secret").compact();
			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
