package com.order.management;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.order.management.entity.Customer;
import com.order.management.service.CustomerService;

@SpringBootApplication
@ComponentScan(basePackages = "com.order.management")
public class OrderManagementApplication {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApplication.class, args);
	}

	@PostConstruct
	public void init() {
		customerService.createCustomer(new Customer("admin", passwordEncoder.encode("admin"), "ADMIN,USER"));
		customerService.createCustomer(new Customer("user1", passwordEncoder.encode("user1"), "USER"));
		customerService.createCustomer(new Customer("user2", passwordEncoder.encode("user2"), "USER"));
	}
}
