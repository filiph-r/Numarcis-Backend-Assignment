package com.example.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom authentication filter for handling user authentication.
 * <p>
 * This filter processes authentication requests and generates JWT tokens upon successful authentication.
 * </p>
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	/**
	 * Constructor for AuthenticationFilter.
	 *
	 * @param authenticationManager the authentication manager
	 * @param jwtUtil the utility class for creating JWT tokens
	 */
	public AuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl(SecurityConstants.USERS_LOGIN_URL);
	}

	/**
	 * Attempts to authenticate the user.
	 * <p>
	 * This method reads the authentication request from the request body and attempts to authenticate the user.
	 * </p>
	 *
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @return the authentication object
	 * @throws AuthenticationException if authentication fails
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			AuthenticationRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
					authRequest.getPassword());
			return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Handles successful authentication.
	 * <p>
	 * This method generates a JWT token for the authenticated user and adds it to the response header.
	 * </p>
	 *
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @param chain the filter chain
	 * @param authResult the authentication result
	 * @throws IOException if an input or output exception occurs
	 * @throws ServletException if a servlet exception occurs
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) authResult.getPrincipal();
		String jwt = jwtUtil.createToken(userDetails.getUsername(), convertAuthoritiesToRoles(userDetails.getAuthorities()));
		response.addHeader(SecurityConstants.JWT_HEADER_STRING, SecurityConstants.JWT_TOKEN_PREFIX + jwt);
	}

	/**
	 * Converts authorities to roles.
	 *
	 * @param authorities the collection of granted authorities
	 * @return a list of role names
	 */
	private List<String> convertAuthoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
	}
}
