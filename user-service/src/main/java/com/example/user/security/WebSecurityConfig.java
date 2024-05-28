package com.example.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
						.requestMatchers(new AntPathRequestMatcher("/users/register"), new AntPathRequestMatcher("/users/login"))
						.permitAll()
						.anyRequest().authenticated()
						.and()
						.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
				);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}


