package com.example.user.security;

/**
 * Class representing an authentication request.
 * <p>
 * This class is used to encapsulate the username and password for user authentication.
 * </p>
 */
public class AuthenticationRequest {

	private String username;
	private String password;

	/**
	 * Default constructor.
	 */
	public AuthenticationRequest() {
	}

	/**
	 * Constructor with parameters.
	 *
	 * @param username the username
	 * @param password the password
	 */
	public AuthenticationRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
