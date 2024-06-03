package com.example.product.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter responsible for JWT authorization.
 * Intercepts requests to validate the JWT token and set the security context.
 */
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

	private static final String UNAUTHORIZED_MESSAGE = "Unauthorized";
	private static final String ROLE_PREFIX = "ROLE_";

	private JwtUtil jwtUtil;

	/**
	 * Constructs an AuthorizationFilter with the specified JwtUtil.
	 *
	 * @param jwtUtil the utility class for handling JWT operations
	 */
	@Autowired
	public AuthorizationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	/**
	 * Filters each request to validate the JWT token and set the security context.
	 *
	 * @param request     the HTTP request
	 * @param response    the HTTP response
	 * @param filterChain the filter chain
	 * @throws ServletException if an error occurs during filtering
	 * @throws IOException      if an input or output error occurs
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = resolveToken(request);

		if (token == null || !jwtUtil.validateToken(token)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
			return;
		}

		String username = jwtUtil.getUsername(token);
		List<String> roles = jwtUtil.getRoles(token);
		List<GrantedAuthority> authorities = roles.stream()
				.map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))  // Prefix roles with "ROLE_"
				.collect(Collectors.toList());
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

	/**
	 * Resolves the JWT token from the HTTP request header.
	 *
	 * @param request the HTTP request
	 * @return the JWT token if present and valid, otherwise null
	 */
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(SecurityConstants.JWT_HEADER_STRING);
		if (bearerToken != null && bearerToken.startsWith(SecurityConstants.JWT_TOKEN_PREFIX)) {
			return bearerToken.substring(SecurityConstants.JWT_TOKEN_PREFIX.length());
		}
		return null;
	}
}
