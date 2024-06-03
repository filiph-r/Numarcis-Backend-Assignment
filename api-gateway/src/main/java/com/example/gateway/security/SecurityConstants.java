package com.example.gateway.security;

/**
 * Constants used for security configuration.
 */
public final class SecurityConstants
{

	// JWT related constants
	/**
	 * Prefix for the JWT token.
	 */
	public static final String JWT_TOKEN_PREFIX = "Bearer ";

	/**
	 * Header string for the JWT token.
	 */
	public static final String JWT_HEADER_STRING = "Authorization";

	// URL patterns
	/**
	 * URL pattern for user-service.
	 */
	public static final String USERS_REGISTER_URL = "/users/register";
	public static final String USERS_LOGIN_URL = "/users/login";

	/**
	 * Private constructor to prevent instantiation.
	 */
	private SecurityConstants()
	{
	}
}