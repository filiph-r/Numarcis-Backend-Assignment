package com.example.order.security;

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

/**
 * Utility class for handling JWT operations.
 */
@Component
public class JwtUtil {

	// Constants used in the JWT utility class
	private static final String JWT_ROLES_CLAIM = "roles";
	private static final String JWT_INVALID_TOKEN_MESSAGE = "Invalid JWT token";
	private static final String JWT_EXPIRED_OR_INVALID_TOKEN_MESSAGE = "Expired or invalid JWT token";

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expirationTime; // in milliseconds

	private Algorithm algorithm;
	private JWTVerifier verifier;

	/**
	 * Constructs a JwtUtil with the given secret key.
	 *
	 * @param secretKey the secret key for signing the JWT
	 */
	public JwtUtil(@Value("${jwt.secret}") String secretKey) {
		this.secretKey = secretKey;
		this.algorithm = Algorithm.HMAC256(secretKey);
		this.verifier = JWT.require(algorithm).build();
	}

	/**
	 * Creates a JWT token for the given username and roles.
	 *
	 * @param username the username to include in the token
	 * @param roles the roles to include in the token
	 * @return the created JWT token
	 */
	public String createToken(String username, List<String> roles) {
		return JWT.create()
				.withSubject(username)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
				.withClaim(JWT_ROLES_CLAIM, roles)
				.sign(algorithm);
	}

	/**
	 * Validates the given JWT token.
	 *
	 * @param token the JWT token to validate
	 * @return true if the token is valid, false otherwise
	 * @throws RuntimeException if the token is expired or invalid
	 */
	public boolean validateToken(String token) {
		try {
			verifier.verify(token);
			return true;
		} catch (JWTVerificationException e) {
			throw new RuntimeException(JWT_EXPIRED_OR_INVALID_TOKEN_MESSAGE, e);
		}
	}

	/**
	 * Retrieves the username from the given JWT token.
	 *
	 * @param token the JWT token
	 * @return the username extracted from the token
	 * @throws RuntimeException if the token is invalid
	 */
	public String getUsername(String token) {
		try {
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getSubject();
		} catch (JWTVerificationException e) {
			throw new RuntimeException(JWT_INVALID_TOKEN_MESSAGE, e);
		}
	}

	/**
	 * Retrieves the roles from the given JWT token.
	 *
	 * @param token the JWT token
	 * @return the list of roles extracted from the token
	 * @throws RuntimeException if the token is invalid
	 */
	public List<String> getRoles(String token) {
		try {
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getClaim(JWT_ROLES_CLAIM).asList(String.class);
		} catch (JWTDecodeException e) {
			throw new RuntimeException(JWT_INVALID_TOKEN_MESSAGE, e);
		}
	}
}
