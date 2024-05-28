package com.example.product.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class JwtTokenProvider
{
	private final String secretKey = "secret";

	public String createToken(String username, List<String> roles)
	{
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("roles", roles);

		Date now = new Date();
		Date validity = new Date(now.getTime() + 3600000); // 1h

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	public boolean validateToken(String token)
	{
		try
		{
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		}
		catch (JwtException | IllegalArgumentException e)
		{
			throw new RuntimeException("Expired or invalid JWT token");
		}
	}

	public String getUsername(String token)
	{
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public List<String> getRoles(String token)
	{
		return (List<String>) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("roles");
	}
}
