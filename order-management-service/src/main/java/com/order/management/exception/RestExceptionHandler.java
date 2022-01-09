package com.order.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleException(AccessDeniedException e) {
		return new ResponseEntity<>(ExceptionResponse.builder().errorMessage(e.getMessage()).build(),
				HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(OrderManagementException.class)
	public ResponseEntity<Object> handleException(OrderManagementException e) {
		return new ResponseEntity<>(ExceptionResponse.builder().errorMessage(e.getMessage()).build(), e.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception e) {
		return new ResponseEntity<>(ExceptionResponse.builder().errorMessage(e.getMessage()).build(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
