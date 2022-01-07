package com.order.management.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.order.management.entity.Customer;
import com.order.management.repository.CustomerRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;

	private Customer customer;

	private static final String USERNAME = "TESTUSER";
	private static final String PASSWORD = "TESTPASSWORD";
	private static final String ROLE = "ADMIN";

	@Before
	public void init() {
		customer = new Customer();
		customer.setUsername(USERNAME);
		customer.setPassword(PASSWORD);
		customer.setRoles(ROLE);
	}

	@Test
	public void createCustomerTestShouldReturnSuccess() {
		when(customerRepository.save(customer)).thenReturn(customer);
		Customer createdCustomer = customerService.createCustomer(customer);
		assertNotNull(createdCustomer);
		assertTrue(createdCustomer.getUsername().equals(customer.getUsername()));
	}

	@Test
	public void loadUserByUsername() throws UsernameNotFoundException {

		when(customerRepository.findByUsername(USERNAME)).thenReturn(customer);
		UserDetails userDetails = customerService.loadUserByUsername(USERNAME);
		assertNotNull(userDetails);
		assertTrue(userDetails.getUsername().equals(customer.getUsername()));

	}

	@Test(expected = UsernameNotFoundException.class)
	public void loadUserByUsernameReturnsException() throws UsernameNotFoundException {

		when(customerRepository.findByUsername(USERNAME)).thenReturn(null);
		customerService.loadUserByUsername(USERNAME);
	}
}
