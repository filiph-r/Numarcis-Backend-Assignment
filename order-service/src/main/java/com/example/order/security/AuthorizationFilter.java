package com.example.order.security;

import com.example.order.model.Order;
import com.example.order.service.OrderService;
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


@Component
public class AuthorizationFilter extends OncePerRequestFilter
{

	private JwtUtil jwtUtil;
	private OrderService orderService;

	@Autowired
	public AuthorizationFilter(JwtUtil jwtUtil, OrderService orderService)
	{
		this.jwtUtil = jwtUtil;
		this.orderService = orderService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		String token = resolveToken(request);

		if (token != null && jwtUtil.validateToken(token))
		{
			String username = jwtUtil.getUsername(token);
			List<String> roles = jwtUtil.getRoles(token);
			List<GrantedAuthority> authorities = roles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
					.collect(Collectors.toList());

			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(auth);

			if (isProtectedOrderEndpoint(request) && !isUserAllowedToAccessOrder(request, username, roles))
			{
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean isProtectedOrderEndpoint(HttpServletRequest request)
	{
		String method = request.getMethod();
		String uri = request.getRequestURI();
		return (uri.startsWith("/orders/") && (method.equals("GET") || method.equals("PUT") || method.equals("DELETE"))) ||
				(uri.startsWith("/orders/user/") && method.equals("GET"));
	}

	private boolean isUserAllowedToAccessOrder(HttpServletRequest request, String username, List<String> roles)
	{
		String uri = request.getRequestURI();

		if (uri.startsWith("/orders/user/"))
		{
			String userIdStr = uri.split("/orders/user/")[1];
			return userIdStr.equals(username) || roles.contains("ADMIN");
		}

		if (uri.startsWith("/orders/"))
		{
			String orderIdStr = uri.split("/orders/")[1];
			Long orderId = Long.parseLong(orderIdStr);
			Order order = orderService.findById(orderId).orElse(null);

			if (order == null)
			{
				return false;
			}

			return order.getUsername().equals(username) || roles.contains("ADMIN");
		}

		return true;
	}

	private String resolveToken(HttpServletRequest request)
	{
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer "))
		{
			return bearerToken.substring(7);
		}
		return null;
	}
}
