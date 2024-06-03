package com.example.user.security;

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
 * <p>
 * This class provides methods for creating, validating, and parsing JWT tokens.
 * </p>
 */
@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expirationTime; // in milliseconds

	private Algorithm algorithm;
	private JWTVerifier verifier;

	private static final String CLAIM_ROLES = "roles";
	private static final String ERROR_EXPIRED_INVALID_TOKEN = "Expired or invalid JWT token";
	private static final String ERROR_INVALID_TOKEN = "Invalid JWT token";

	/**
	 * Constructs a JwtUtil with the specified secret key.
	 *
	 * @param secretKey the secret key used for signing the JWT tokens
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
				.withClaim(CLAIM_ROLES, roles)
				.sign(algorithm);
	}

	/**
	 * Validates the given JWT token.
	 *
	 * @param token the JWT token to validate
	 * @return true if the token is valid, false otherwise
	 */
	public boolean validateToken(String token) {
		try {
			verifier.verify(token);
			return true;
		} catch (JWTVerificationException e) {
			throw new RuntimeException(ERROR_EXPIRED_INVALID_TOKEN, e);
		}
	}

	/**
	 * Gets the username from the given JWT token.
	 *
	 * @param token the JWT token
	 * @return the username extracted from the token
	 */
	public String getUsername(String token) {
		try {
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getSubject();
		} catch (JWTVerificationException e) {
			throw new RuntimeException(ERROR_INVALID_TOKEN, e);
		}
	}

	/**
	 * Gets the roles from the given JWT token.
	 *
	 * @param token the JWT token
	 * @return the list of roles extracted from the token
	 */
	public List<String> getRoles(String token) {
		try {
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getClaim(CLAIM_ROLES).asList(String.class);
		} catch (JWTDecodeException e) {
			throw new RuntimeException(ERROR_INVALID_TOKEN, e);
		}
	}
}
