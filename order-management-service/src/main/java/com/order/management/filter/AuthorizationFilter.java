package com.order.management.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.management.helper.JwtHelper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER_TOKEN = "Bearer ";

	@Autowired
	private JwtHelper jwtHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().equals("/authenticate") || request.getServletPath().equals("/token/refresh")) {
			filterChain.doFilter(request, response);
		} else {

			try {
				String authHeader = request.getHeader(AUTH_HEADER);

				if (Strings.isNotBlank(authHeader) && authHeader.startsWith(BEARER_TOKEN)) {
					String authToken = authHeader.substring(BEARER_TOKEN.length());

					Date expiry = jwtHelper.extractTokenExpiry(authToken);

					if (new Date().before(expiry)) {

						String username = jwtHelper.extractUsername(authToken);

						List<String> roles = jwtHelper.extractClaims(authToken);

						System.out.println("ROLES :: " + roles);
						List<SimpleGrantedAuthority> authorities = roles.stream()
								.map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
						SecurityContextHolder.getContext().setAuthentication(
								new UsernamePasswordAuthenticationToken(username, null, authorities));
					}

				}

				filterChain.doFilter(request, response);
			} catch (Exception e) {
				log.error("Invalid Token was passed",e);
				ObjectMapper mapper = new ObjectMapper();
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				Map<String, String> errors = new HashMap<>();
				errors.put("errorMessage", e.getMessage());
				mapper.writeValue(response.getOutputStream(), errors);
			}
		}
	}
}
