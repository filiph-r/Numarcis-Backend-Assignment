package com.example.product.security;

/**
 * Constants used for security configuration.
 */
public final class SecurityConstants {

	// JWT related constants
	/**
	 * Prefix for the JWT token.
	 */
	public static final String JWT_TOKEN_PREFIX = "Bearer ";

	/**
	 * Header string for the JWT token.
	 */
	public static final String JWT_HEADER_STRING = "Authorization";

	// Role based constants
	/**
	 * Role constant for users.
	 */
	public static final String ROLE_USER = "USER";

	/**
	 * Role constant for administrators.
	 */
	public static final String ROLE_ADMIN = "ADMIN";

	// URL patterns
	/**
	 * URL pattern for products.
	 */
	public static final String PRODUCTS_URL = "/products/**";

	/**
	 * Private constructor to prevent instantiation.
	 */
	private SecurityConstants() {
	}
}
