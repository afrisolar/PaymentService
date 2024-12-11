package com.afrisol.PaymentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long paymentId;
    private String product;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String currency;
    private String customer;
}

