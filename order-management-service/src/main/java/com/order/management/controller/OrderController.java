package com.order.management.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.management.dto.OrderRequestDto;
import com.order.management.entity.Order;
import com.order.management.exception.OrderManagementException;
import com.order.management.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	@CrossOrigin(origins = "*")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto)
			throws OrderManagementException {
		log.info("Creating an Order: {}", orderRequestDto);
		return new ResponseEntity<>(orderService.createOrder(orderRequestDto), HttpStatus.CREATED);
	}

	@GetMapping
	@CrossOrigin(origins = "*")
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public ResponseEntity<List<Order>> getAllOrders() {
		log.info("Getting all the orders");
		List<Order> orders = orderService.getAllOrders();
		return new ResponseEntity<>((orders == null || orders.isEmpty()) ? new ArrayList<Order>() : orders, HttpStatus.OK);
	}

	@GetMapping("/{orderId}")
	@CrossOrigin(origins = "*")
	@PreAuthorize("hasAuthority('USER','ADMIN')")
	public ResponseEntity<Order> findOrder(@PathVariable String orderId) throws OrderManagementException {
		log.info("Finding an order with Order ID: {}", orderId);
		return new ResponseEntity<>(orderService.findOrder(Long.valueOf(orderId)), HttpStatus.OK);
	}

	@PutMapping("/{orderId}")
	@CrossOrigin(origins = "*")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Order> updateOrder(@PathVariable String orderId, OrderRequestDto orderRequestDto)
			throws Exception {
		log.info("Updating order with Order ID: {}", orderId);
		Order order = orderService.updateOrder(Long.valueOf(orderId), orderRequestDto);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@DeleteMapping("/{orderId}")
	@CrossOrigin(origins = "*")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteOrder(@PathVariable String orderId) throws Exception {
		log.info("Deleting order with Order ID: {}", orderId);
		orderService.deleteOrder(Long.valueOf(orderId));
		return new ResponseEntity<>("Order has been deleted", HttpStatus.OK);
	}

}
