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


@Configuration
public class DefaultAdminInitializer {

	private final CustomUserDetailsService userDetailsService;
	private final AdminConfig adminConfig;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public DefaultAdminInitializer(CustomUserDetailsService userDetailsService, AdminConfig adminConfig, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.adminConfig = adminConfig;
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public ApplicationRunner initializer() {
		return args -> {
			if (userDetailsService.findByUsername(adminConfig.getUsername()) == null) {
				final User admin = new User();
				admin.setUsername(adminConfig.getUsername());
				admin.setPassword(passwordEncoder.encode(adminConfig.getPassword()));
				admin.setRoles(Collections.singletonList("ADMIN"));
				userDetailsService.save(admin);
			}
		};
	}
}
