package com.example.order.security;

import com.example.order.model.Order;
import com.example.order.service.interfaces.OrderService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
 * Filter for handling authorization based on JWT tokens.
 */
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

	private JwtUtil jwtUtil;
	private OrderService orderService;

	private static final String ORDERS_PATH = "/orders/";
	private static final String ORDERS_USERS_PATH = "/orders/user/";
	private static final String ROLE = "ROLE_";

	/**
	 * Constructs an AuthorizationFilter with the given JwtUtil and OrderService.
	 *
	 * @param jwtUtil the JWT utility for token operations
	 * @param orderService the service for managing orders
	 */
	@Autowired
	public AuthorizationFilter(JwtUtil jwtUtil, OrderService orderService) {
		this.jwtUtil = jwtUtil;
		this.orderService = orderService;
	}

	/**
	 * Filters incoming requests to handle authorization.
	 *
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @param filterChain the filter chain
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = resolveToken(request);

		if (token != null && jwtUtil.validateToken(token)) {
			String username = jwtUtil.getUsername(token);
			List<String> roles = jwtUtil.getRoles(token);
			List<GrantedAuthority> authorities = roles.stream()
					.map(role -> new SimpleGrantedAuthority(ROLE + role))
					.collect(Collectors.toList());

			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(auth);

			if (isProtectedOrderEndpoint(request) && !isUserAllowedToAccessOrder(request, username, roles)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Checks if the request is targeting a protected order endpoint.
	 *
	 * @param request the HTTP request
	 * @return true if the request targets a protected order endpoint, false otherwise
	 */
	private boolean isProtectedOrderEndpoint(HttpServletRequest request) {
		String method = request.getMethod();
		String uri = request.getRequestURI();
		return (uri.startsWith(ORDERS_PATH) && (method.equals(HttpMethod.GET.toString()) || method.equals(HttpMethod.PUT.toString())
				|| method.equals(HttpMethod.DELETE.toString()))) ||
				(uri.startsWith(ORDERS_USERS_PATH) && method.equals(HttpMethod.GET.toString()));
	}

	/**
	 * Checks if the user is allowed to access the order.
	 *
	 * @param request the HTTP request
	 * @param username the username from the JWT token
	 * @param roles the roles from the JWT token
	 * @return true if the user is allowed to access the order, false otherwise
	 */
	private boolean isUserAllowedToAccessOrder(HttpServletRequest request, String username, List<String> roles) {
		String uri = request.getRequestURI();

		if (uri.startsWith(ORDERS_USERS_PATH)) {
			String userIdStr = uri.split(ORDERS_USERS_PATH)[1];
			return userIdStr.equals(username) || roles.contains(SecurityConstants.ROLE_ADMIN);
		}

		if (uri.startsWith(ORDERS_PATH)) {
			String orderIdStr = uri.split(ORDERS_PATH)[1];
			Long orderId = Long.parseLong(orderIdStr);
			Order order = orderService.findById(orderId).orElse(null);

			if (order == null) {
				return false;
			}

			return order.getUsername().equals(username) || roles.contains(SecurityConstants.ROLE_ADMIN);
		}

		return true;
	}

	/**
	 * Resolves the JWT token from the HTTP request.
	 *
	 * @param request the HTTP request
	 * @return the JWT token, or null if not found
	 */
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(SecurityConstants.JWT_HEADER_STRING);
		if (bearerToken != null && bearerToken.startsWith(SecurityConstants.JWT_TOKEN_PREFIX)) {
			return bearerToken.substring(SecurityConstants.JWT_TOKEN_PREFIX.length());
		}
		return null;
	}
}
