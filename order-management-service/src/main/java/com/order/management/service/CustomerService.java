package com.order.management.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.order.management.entity.Customer;
import com.order.management.repository.CustomerRepository;

@Service
public class CustomerService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	public Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Customer customer = customerRepository.findByUsername(username);

		if (customer != null) {
			List<SimpleGrantedAuthority> authorities = null;
			if (customer.getRoles() != null) {
				authorities = Arrays.asList(customer.getRoles().split(",")).stream()
						.map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
			}

			return new User(customer.getUsername(), customer.getPassword(), authorities);
		} else {
			throw new UsernameNotFoundException("User " + username + " does not exist");
		}

	}
}
