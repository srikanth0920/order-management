package com.order.management.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExceptionResponse {
	
	private String errorMessage;
	
}
