package com.order.management.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.order.management.dto.AuthenticationRequest;
import com.order.management.dto.AuthenticationResponse;
import com.order.management.dto.RefreshJwtRequest;
import com.order.management.exception.OrderManagementException;
import com.order.management.helper.JwtHelper;
import com.order.management.service.CustomerService;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private CustomerService customerService;

	@Mock
	private JwtHelper jwtHelper;

	@InjectMocks
	private LoginController loginController;

	private static final String USERNAME = "TESTUSER";
	private static final String PASSWORD = "TESTPASSWORD";
	private static final String ROLE = "ADMIN";
	private static final String ACCESS_TOKEN = "ACCESS-TOKEN";
	private static final String REFRESH_TOKEN = "REFRESH-TOKEN";

	@Before
	public void init() {

		UserDetails userDetails = new User(USERNAME, PASSWORD, Arrays.asList(new SimpleGrantedAuthority(ROLE)));

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(Mockito.mock(UsernamePasswordAuthenticationToken.class));

		SecurityContext securityContext = Mockito.mock(SecurityContext.class);

		SecurityContextHolder.setContext(securityContext);
		when(customerService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
		when(jwtHelper.generateAccessToken(any(String.class), any(List.class))).thenReturn(ACCESS_TOKEN);
		when(jwtHelper.generateRefreshToken(any(String.class), any(List.class))).thenReturn(REFRESH_TOKEN);
	}

	@Test
	public void authenticateTestShouldReturnTokens() throws OrderManagementException {

		ResponseEntity<AuthenticationResponse> authenticationResponse = loginController
				.authenticate(AuthenticationRequest.builder().username(USERNAME).password(PASSWORD).build());

		assertNotNull(authenticationResponse);
		assertNotNull(authenticationResponse.getBody());
		assertTrue(ACCESS_TOKEN.equals(authenticationResponse.getBody().getAccessToken()));
		assertTrue(REFRESH_TOKEN.equals(authenticationResponse.getBody().getRefreshToken()));

	}

	@Test
	public void refreshTokenTestShouldReturnTokens() throws OrderManagementException {

		when(jwtHelper.extractUsername(REFRESH_TOKEN)).thenReturn(USERNAME);

		ResponseEntity<AuthenticationResponse> authenticationResponse = loginController
				.generateAccessTokens(RefreshJwtRequest.builder().refreshToken(REFRESH_TOKEN).build());

		assertNotNull(authenticationResponse);
		assertNotNull(authenticationResponse.getBody());
		assertTrue(ACCESS_TOKEN.equals(authenticationResponse.getBody().getAccessToken()));
		assertTrue(REFRESH_TOKEN.equals(authenticationResponse.getBody().getRefreshToken()));

	}
}
