package com.example.user.security;

import com.example.user.config.AdminConfig;
import com.example.user.model.User;
import com.example.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

/**
 * Configuration class for initializing the default admin user.
 * <p>
 * This class checks if the admin user exists on application startup and creates one if it does not.
 * </p>
 */
@Configuration
public class DefaultAdminInitializer {

	private final CustomUserDetailsService userDetailsService;
	private final AdminConfig adminConfig;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Constructs a new DefaultAdminInitializer.
	 *
	 * @param userDetailsService the custom user details service
	 * @param adminConfig the admin configuration
	 * @param passwordEncoder the password encoder
	 */
	@Autowired
	public DefaultAdminInitializer(CustomUserDetailsService userDetailsService, AdminConfig adminConfig, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.adminConfig = adminConfig;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Initializes the default admin user.
	 * <p>
	 * This method checks if the admin user exists. If not, it creates the admin user with the username and password
	 * from the configuration and assigns the admin role.
	 * </p>
	 *
	 * @return an ApplicationRunner that performs the initialization
	 */
	@Bean
	public ApplicationRunner initializer() {
		return args -> {
			if (userDetailsService.findByUsername(adminConfig.getUsername()) == null) {
				final User admin = new User();
				admin.setUsername(adminConfig.getUsername());
				admin.setPassword(passwordEncoder.encode(adminConfig.getPassword()));
				admin.setRoles(Collections.singletonList(SecurityConstants.ROLE_ADMIN));
				userDetailsService.save(admin);
			}
		};
	}
}
