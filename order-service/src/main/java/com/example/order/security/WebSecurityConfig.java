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


@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{

	private AuthorizationFilter authorizationFilter;

	@Autowired
	public WebSecurityConfig(AuthorizationFilter authorizationFilter)
	{
		this.authorizationFilter = authorizationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authz -> authz
						.requestMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers(HttpMethod.GET, "/orders/**").authenticated()
						.requestMatchers(HttpMethod.PUT, "/orders/**").authenticated()
						.requestMatchers(HttpMethod.DELETE, "/orders/**").authenticated()
						.anyRequest().authenticated()
				)
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
