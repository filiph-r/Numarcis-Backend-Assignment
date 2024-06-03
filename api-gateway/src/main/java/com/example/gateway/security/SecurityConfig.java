package com.example.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuration class for setting up security filters and authorization in a Spring WebFlux application.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private final AuthorizationFilter authorizationFilter;

	/**
	 * Constructs a new SecurityConfig with the given AuthorizationFilter.
	 *
	 * @param authorizationFilter the authorization filter to be used
	 */
	@Autowired
	public SecurityConfig(AuthorizationFilter authorizationFilter) {
		this.authorizationFilter = authorizationFilter;
	}

	/**
	 * Defines the security filter chain for the application.
	 *
	 * @param http the ServerHttpSecurity object to configure
	 * @return the configured SecurityWebFilterChain
	 */
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange(authorize -> authorize
						.anyExchange().permitAll()
				)
				.addFilterAt(authorizationFilter, SecurityWebFiltersOrder.AUTHORIZATION);

		return http.build();
	}
}
