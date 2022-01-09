package com.order.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.order.management.dto.OrderRequestDto;
import com.order.management.entity.Order;
import com.order.management.exception.OrderManagementException;
import com.order.management.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	private static final String ORDER_NOT_FOUND_ERROR = "Order not found";
	private static final String ORDER_CREATION_ERROR = "Error creating an Order";

	public Order createOrder(OrderRequestDto orderRequestDto) throws OrderManagementException {

		Order order = new Order();
		try {
			order.setOrderType(orderRequestDto.getOrderType());
			order.setOrderDescription(orderRequestDto.getOrderDescription());
			orderRepository.save(order);
		} catch (Exception e) {
			throw new OrderManagementException(HttpStatus.INTERNAL_SERVER_ERROR, ORDER_CREATION_ERROR);
		}
		return order;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Order findOrder(long orderId) throws OrderManagementException {
		Optional<Order> optional = orderRepository.findById(orderId);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new OrderManagementException(HttpStatus.NOT_FOUND, ORDER_NOT_FOUND_ERROR);
		}

	}

	public Order updateOrder(long orderId, OrderRequestDto orderRequestDto) {
		Optional<Order> optional = orderRepository.findById(orderId);
		Order order = null;
		if (optional.isPresent()) {
			order = optional.get();
			order.setOrderDescription(orderRequestDto.getOrderDescription());
			order.setOrderType(orderRequestDto.getOrderType());

			orderRepository.save(order);
		}

		return order;
	}

	public void deleteOrder(long orderId) {
		orderRepository.deleteById(orderId);
	}

}
