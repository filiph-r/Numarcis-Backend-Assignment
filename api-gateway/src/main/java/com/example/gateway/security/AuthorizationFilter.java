package com.example.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Authorization filter that intercepts requests to validate JWT tokens.
 */
@Component
public class AuthorizationFilter implements WebFilter {

	private final JwtUtil jwtUtil;

	/**
	 * Constructs a new AuthorizationFilter with the given JwtUtil.
	 *
	 * @param jwtUtil the utility class for JWT operations
	 */
	@Autowired
	public AuthorizationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	/**
	 * Filters incoming requests to check for valid JWT tokens.
	 *
	 * @param exchange the current server exchange
	 * @param filterChain the web filter chain
	 * @return a Mono<Void> that indicates filter chain processing
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) {
		String path = exchange.getRequest().getURI().getPath();

		if (path.equals(SecurityConstants.USERS_REGISTER_URL) || path.equals(SecurityConstants.USERS_LOGIN_URL)) {
			// Permit these paths without authentication
			return filterChain.filter(exchange);
		}

		String token = resolveToken(exchange);

		try {
			if (token == null || !jwtUtil.validateToken(token)) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}
		} catch (RuntimeException e) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		return filterChain.filter(exchange);
	}

	/**
	 * Resolves the JWT token from the request headers.
	 *
	 * @param exchange the current server exchange
	 * @return the JWT token if present, otherwise null
	 */
	private String resolveToken(ServerWebExchange exchange) {
		String bearerToken = exchange.getRequest().getHeaders().getFirst(SecurityConstants.JWT_HEADER_STRING);
		if (bearerToken != null && bearerToken.startsWith(SecurityConstants.JWT_TOKEN_PREFIX)) {
			return bearerToken.substring(SecurityConstants.JWT_TOKEN_PREFIX.length());
		}
		return null;
	}
}
