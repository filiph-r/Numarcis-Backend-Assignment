package com.example.user.security;

import com.example.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuration class for web security.
 * <p>
 * This class configures security settings for the application, including authentication, authorization,
 * and password encoding.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtil jwtUtil;

	/**
	 * Constructs a new WebSecurityConfig.
	 *
	 * @param customUserDetailsService the custom user details service
	 * @param jwtUtil the JWT utility
	 */
	@Autowired
	public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtUtil = jwtUtil;
	}

	/**
	 * Configures the security filter chain.
	 * <p>
	 * This method sets up the HTTP security, including disabling CSRF, setting session management to stateless,
	 * configuring authorization rules, and adding the custom authentication filter.
	 * </p>
	 *
	 * @param http the HttpSecurity object
	 * @param authenticationManager the authentication manager
	 * @return the configured SecurityFilterChain
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, jwtUtil);

		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authz -> authz
						.requestMatchers(
								new AntPathRequestMatcher(SecurityConstants.USERS_REGISTER_URL),
								new AntPathRequestMatcher(SecurityConstants.USERS_LOGIN_URL)
						).permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * Configures the authentication manager.
	 *
	 * @param authenticationConfiguration the authentication configuration
	 * @return the configured AuthenticationManager
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * Configures the password encoder.
	 *
	 * @return the PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures the DAO authentication provider.
	 *
	 * @return the DaoAuthenticationProvider
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}
