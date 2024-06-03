package com.example.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for admin credentials.
 * <p>
 * This class is used to load the admin username and password from the
 * application properties file.
 * </p>
 */
@Configuration
public class AdminConfig {

	/**
	 * Admin username loaded from application properties.
	 */
	@Value("${admin.username}")
	private String username;

	/**
	 * Admin password loaded from application properties.
	 */
	@Value("${admin.password}")
	private String password;

	/**
	 * Gets the admin username.
	 *
	 * @return the admin username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the admin password.
	 *
	 * @return the admin password
	 */
	public String getPassword() {
		return password;
	}
}
