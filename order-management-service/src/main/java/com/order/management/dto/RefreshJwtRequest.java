package com.order.management.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshJwtRequest {

	private String refreshToken;

}
