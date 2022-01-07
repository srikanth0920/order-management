package com.order.management.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class OrderManagementException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus status;
	private String message;

	public OrderManagementException(HttpStatus status, String message) {
		super(message);
		this.message = message;
		this.status = status;
	}
}
