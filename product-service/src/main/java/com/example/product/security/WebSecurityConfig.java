package com.example.product.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuration class for web security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private AuthorizationFilter authorizationFilter;

	/**
	 * Constructs a WebSecurityConfig with the specified AuthorizationFilter.
	 *
	 * @param authorizationFilter the authorization filter to use
	 */
	@Autowired
	public WebSecurityConfig(AuthorizationFilter authorizationFilter) {
		this.authorizationFilter = authorizationFilter;
	}

	/**
	 * Configures the security filter chain.
	 *
	 * @param http the HttpSecurity to modify
	 * @return the configured SecurityFilterChain
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers(new AntPathRequestMatcher(SecurityConstants.PRODUCTS_URL, HttpMethod.GET.name())).permitAll()
						.requestMatchers(new AntPathRequestMatcher(SecurityConstants.PRODUCTS_URL, HttpMethod.POST.name())).hasRole(SecurityConstants.ROLE_ADMIN)
						.requestMatchers(new AntPathRequestMatcher(SecurityConstants.PRODUCTS_URL, HttpMethod.PUT.name())).hasRole(SecurityConstants.ROLE_ADMIN)
						.requestMatchers(new AntPathRequestMatcher(SecurityConstants.PRODUCTS_URL, HttpMethod.DELETE.name())).hasRole(SecurityConstants.ROLE_ADMIN)
						.anyRequest().authenticated()
				)
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
