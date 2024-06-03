package com.example.order.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for web security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final AuthorizationFilter authorizationFilter;

	/**
	 * Constructs a WebSecurityConfig with the given AuthorizationFilter.
	 *
	 * @param authorizationFilter the authorization filter
	 */
	@Autowired
	public WebSecurityConfig(AuthorizationFilter authorizationFilter) {
		this.authorizationFilter = authorizationFilter;
	}

	/**
	 * Configures the security filter chain.
	 *
	 * @param http the HttpSecurity object
	 * @return the configured SecurityFilterChain
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authz -> authz
						.requestMatchers(HttpMethod.POST, SecurityConstants.ORDERS_URL).hasAnyRole(SecurityConstants.ROLE_USER, SecurityConstants.ROLE_ADMIN)
						.requestMatchers(HttpMethod.GET, SecurityConstants.ORDERS_URL).authenticated()
						.requestMatchers(HttpMethod.PUT, SecurityConstants.ORDERS_URL).authenticated()
						.requestMatchers(HttpMethod.DELETE, SecurityConstants.ORDERS_URL).authenticated()
						.anyRequest().authenticated()
				)
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
