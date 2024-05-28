package com.example.product.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expirationTime; // in milliseconds

	private Algorithm algorithm;
	private JWTVerifier verifier;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		this.secretKey = secretKey;
		this.algorithm = Algorithm.HMAC256(secretKey);
		this.verifier = JWT.require(algorithm).build();
	}

	public String createToken(String username, List<String> roles) {
		return JWT.create()
				.withSubject(username)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
				.withClaim("roles", roles)
				.sign(algorithm);
	}

	public boolean validateToken(String token) {
		try {
			verifier.verify(token);
			return true;
		} catch (JWTVerificationException e) {
			throw new RuntimeException("Expired or invalid JWT token", e);
		}
	}

	public String getUsername(String token) {
		try {
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getSubject();
		} catch (JWTVerificationException e) {
			throw new RuntimeException("Invalid JWT token", e);
		}
	}

	public List<String> getRoles(String token) {
		try {
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getClaim("roles").asList(String.class);
		} catch (JWTDecodeException e) {
			throw new RuntimeException("Invalid JWT token", e);
		}
	}
}
