package com.order.management.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtHelper {

	@Value("${jwt.secret}")
	private String secret;

	private static final int ACCESS_TOKEN_VALIDITY_MINS = 15;
	private static final int REFRESH_TOKEN_VALIDITY_MINS = 30;
	private static final String ROLES = "roles";

	public String generateAccessToken(String username, List<String> roles) {
		return JWT.create().withIssuer(secret).withSubject(username)
				.withExpiresAt(Date.from(LocalDateTime.now().plusSeconds(ACCESS_TOKEN_VALIDITY_MINS * 60)
						.atZone(ZoneId.systemDefault()).toInstant()))
				.withClaim(ROLES, roles).sign(Algorithm.HMAC512(secret));
	}

	public String generateRefreshToken(String username, List<String> roles) {
		return JWT.create().withIssuer(secret).withSubject(username)
				.withExpiresAt(Date.from(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_VALIDITY_MINS * 60)
						.atZone(ZoneId.systemDefault()).toInstant()))
				.withClaim(ROLES, roles).sign(Algorithm.HMAC512(secret));
	}

	public String extractUsername(String token) {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret)).build();
		verifier.verify(token);
		return JWT.decode(token).getSubject();
	}

	public Date extractTokenExpiry(String token) {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret)).build();
		verifier.verify(token);
		return JWT.decode(token).getExpiresAt();
	}

	public List<String> extractClaims(String authToken) {
		return Arrays.asList(JWT.decode(authToken).getClaim(ROLES).asArray(String.class));
	}
}
