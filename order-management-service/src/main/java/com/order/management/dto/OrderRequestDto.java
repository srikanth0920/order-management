package com.order.management.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class OrderRequestDto {

	@NotBlank(message = "Order Type is mandatory")
	private String orderType;

	@NotEmpty(message = "Order Description is mandatory")
	private String orderDescription;

}
