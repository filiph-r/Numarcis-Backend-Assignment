package com.example.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
				.csrf().disable()
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers(new AntPathRequestMatcher("/users/register"), new AntPathRequestMatcher("/users/login"))
						.permitAll()
						.anyRequest().authenticated()
				);
		return http.build();
	}
}


