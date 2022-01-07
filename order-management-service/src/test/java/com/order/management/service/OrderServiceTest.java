package com.order.management.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.order.management.dto.OrderRequestDto;
import com.order.management.entity.Order;
import com.order.management.exception.OrderManagementException;
import com.order.management.repository.OrderRepository;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	private OrderRequestDto orderRequestDto;

	private final long ORDER_ID = 1;

	@Before
	public void init() {
		orderRequestDto = OrderRequestDto.builder().orderDescription("ORDER_DESCRIPTION").orderType("ORDER_TYPE")
				.build();
	}

	@Test
	public void createOrderTestShouldReturnsSuccess() throws OrderManagementException {
		Order order = orderService.createOrder(orderRequestDto);
		assertNotNull(order);
	}

	@Test
	public void getAllOrdersTestShouldReturnsSuccess() {
		Order mockedOrder = Mockito.mock(Order.class);
		when(orderRepository.findAll()).thenReturn(Stream.of(mockedOrder).collect(Collectors.toList()));
		List<Order> orders = orderService.getAllOrders();
		assertNotNull(orders);
	}

	@Test
	public void findOrderTestShouldReturnsSuccess() throws OrderManagementException {
		Order mockedOrder = Mockito.mock(Order.class);
		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(mockedOrder));
		Order order = orderService.findOrder(ORDER_ID);
		assertNotNull(order);
	}

	@Test(expected = OrderManagementException.class)
	public void findOrderReturnsException() throws OrderManagementException {
		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());
		orderService.findOrder(ORDER_ID);
	}

	@Test
	public void updateOrderTestShouldReturnSuccess() {

		Order mockedOrder = Mockito.mock(Order.class);
		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(mockedOrder));
		when(orderRepository.save(mockedOrder)).thenReturn(mockedOrder);
		orderService.updateOrder(ORDER_ID, orderRequestDto);
	}

	@Test
	public void deleteOrderShouldReturnSuccess() {
		doNothing().when(orderRepository).deleteById(ORDER_ID);
		orderService.deleteOrder(ORDER_ID);
		verify(orderRepository, times(1)).deleteById(ORDER_ID);
	}

}
