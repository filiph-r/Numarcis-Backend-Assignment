package com.example.order.security;

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
	 * URL pattern for orders.
	 */
	public static final String ORDERS_URL = "/orders/**";

	/**
	 * Base URL for the product service.
	 */
	public static final String PRODUCT_SERVICE_URL = "http://PRODUCT-SERVICE/products/";

	/**
	 * Private constructor to prevent instantiation.
	 */
	private SecurityConstants() {
	}
}
