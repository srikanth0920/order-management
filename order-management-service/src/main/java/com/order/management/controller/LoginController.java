package com.order.management.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.management.dto.AuthenticationRequest;
import com.order.management.dto.AuthenticationResponse;
import com.order.management.dto.RefreshJwtRequest;
import com.order.management.exception.OrderManagementException;
import com.order.management.helper.JwtHelper;
import com.order.management.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@Slf4j
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private JwtHelper jwtHelper;

	private static final String BAD_CREDENTIALS_EXCEPTION = "Username or Password is Invalid";
	private static final String INVALID_TOKEN_EXCEPTION = "Token is Invalid";
	private static final String USER_AUTHENTICATION_EXCEPTION = "Error authenticating user";
	private static final String TOKEN_GENERATION_ERROR = "Error generating Access token";

	@GetMapping("/user")
	@CrossOrigin(origins = "*")
	public Principal login(Principal user) throws Exception {
		return user;
	}

	@PostMapping("/authenticate")
	@CrossOrigin(origins = "*")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest)
			throws OrderManagementException {

		AuthenticationResponse authenticationResponse = null;

		try {

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

			UserDetails userDetails = customerService.loadUserByUsername(authenticationRequest.getUsername());

			List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			authenticationResponse = AuthenticationResponse.builder()
					.accessToken(jwtHelper.generateAccessToken(userDetails.getUsername(), roles))
					.refreshToken(jwtHelper.generateRefreshToken(userDetails.getUsername(), roles)).build();

			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), null, userDetails.getAuthorities()));
		} catch (BadCredentialsException e) {
			log.error(USER_AUTHENTICATION_EXCEPTION, e);
			throw new OrderManagementException(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS_EXCEPTION);
		}

		return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);

	}

	@PostMapping("/token/refresh")
	public ResponseEntity<AuthenticationResponse> generateAccessTokens(@RequestBody RefreshJwtRequest refreshJwtRequest)
			throws OrderManagementException {

		AuthenticationResponse authenticationResponse = null;

		try {

			String username = jwtHelper.extractUsername(refreshJwtRequest.getRefreshToken());

			UserDetails userDetails = customerService.loadUserByUsername(username);

			List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			authenticationResponse = AuthenticationResponse.builder()
					.accessToken(jwtHelper.generateAccessToken(userDetails.getUsername(), roles))
					.refreshToken(jwtHelper.generateRefreshToken(userDetails.getUsername(), roles)).build();
		} catch (Exception e) {
			log.error(TOKEN_GENERATION_ERROR, e);
			throw new OrderManagementException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN_EXCEPTION);
		}
		return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);

	}
}
