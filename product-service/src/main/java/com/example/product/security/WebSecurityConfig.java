package com.example.product.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{

	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	public WebSecurityConfig(JwtTokenProvider jwtTokenProvider)
	{
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
				.csrf().disable()
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers(new AntPathRequestMatcher("/products/**", HttpMethod.GET.name())).permitAll()
						.requestMatchers(new AntPathRequestMatcher("/products/**", HttpMethod.POST.name())).hasRole("ADMIN")
						.requestMatchers(new AntPathRequestMatcher("/products/**", HttpMethod.PUT.name())).hasRole("ADMIN")
						.requestMatchers(new AntPathRequestMatcher("/products/**", HttpMethod.DELETE.name())).hasRole("ADMIN")
						.anyRequest().authenticated()
						.and()
						.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
				);
		return http.build();
	}
}

