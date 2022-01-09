package com.order.management.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.order.management.dto.OrderRequestDto;
import com.order.management.entity.Order;
import com.order.management.exception.OrderManagementException;
import com.order.management.service.OrderService;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

	@Mock
	private OrderService orderService;

	@InjectMocks
	private OrderController orderController;

	private OrderRequestDto orderRequestDto;

	private final String ORDER_ID = "1";

	@Before
	public void init() {
		orderRequestDto = OrderRequestDto.builder().orderDescription("ORDER_DESCRIPTION").orderType("ORDER_TYPE")
				.build();
	}

	@Test
	public void createOrderTestShouldReturnSuccess() throws OrderManagementException {

		when(orderService.createOrder(orderRequestDto)).thenReturn(Mockito.mock(Order.class));
		ResponseEntity<Order> orderResponse = orderController.createOrder(orderRequestDto);
		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getBody());
		assertTrue(HttpStatus.CREATED.equals(orderResponse.getStatusCode()));
	}

	@Test
	public void getAllOrdersTestShouldReturnSuccess() {

		List<Order> orders = Stream.of(Mockito.mock(Order.class)).collect(Collectors.toList());
		when(orderService.getAllOrders()).thenReturn(orders);
		ResponseEntity<List<Order>> orderResponse = orderController.getAllOrders();
		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getBody());
		assertTrue(HttpStatus.OK.equals(orderResponse.getStatusCode()));
	}

	@Test
	public void findOrderTestShouldReturnSuccess() throws OrderManagementException {

		when(orderService.findOrder(Long.valueOf(ORDER_ID))).thenReturn(Mockito.mock(Order.class));
		ResponseEntity<Order> orderResponse = orderController.findOrder(ORDER_ID);
		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getBody());
		assertTrue(HttpStatus.OK.equals(orderResponse.getStatusCode()));
	}

	@Test
	public void updateOrder() throws Exception {
		when(orderService.updateOrder(Long.valueOf(ORDER_ID), orderRequestDto)).thenReturn(Mockito.mock(Order.class));
		ResponseEntity<Order> orderResponse = orderController.updateOrder(ORDER_ID, orderRequestDto);
		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getBody());
		assertTrue(HttpStatus.OK.equals(orderResponse.getStatusCode()));
	}

	@Test
	public void deleteOrder() throws Exception {
		doNothing().when(orderService).deleteOrder(Long.valueOf(ORDER_ID));
		ResponseEntity<String> orderResponse = orderController.deleteOrder(ORDER_ID);
		verify(orderService, times(1)).deleteOrder(Long.valueOf(ORDER_ID));
		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getBody());
		assertTrue(HttpStatus.OK.equals(orderResponse.getStatusCode()));
	}

	@Test(expected = RuntimeException.class)
	public void createOrderTestReturnsException() throws OrderManagementException {

		when(orderService.createOrder(orderRequestDto)).thenThrow(RuntimeException.class);
		ResponseEntity<Order> orderResponse = orderController.createOrder(orderRequestDto);
		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getBody());
		assertTrue(HttpStatus.INTERNAL_SERVER_ERROR.equals(orderResponse.getStatusCode()));
	}

	
	@Test(expected = OrderManagementException.class)
	public void findOrderTestReturnsOrderNotFoundException() throws OrderManagementException {

		when(orderService.findOrder(Long.valueOf(ORDER_ID))).thenThrow(new OrderManagementException(HttpStatus.NOT_FOUND, "Error"));
		ResponseEntity<Order> orderResponse = orderController.findOrder(ORDER_ID);
		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getBody());
		assertTrue(HttpStatus.NOT_FOUND.equals(orderResponse.getStatusCode()));
	}
	
	@Test
	public void getAllOrdersTestShouldReturnOkWhenThereAreNoOrders() {

		when(orderService.getAllOrders()).thenReturn(null);
		ResponseEntity<List<Order>> orderResponse = orderController.getAllOrders();
		assertNotNull(orderResponse);
		assertNotNull(orderResponse.getBody());
		assertTrue(HttpStatus.OK.equals(orderResponse.getStatusCode()));
	}
}
