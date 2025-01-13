package com.afrisol.PaymentService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    @NotBlank(message = "Product name is required")
    private String product;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Card number is required")
    @Min(value = 1, message = "Card number must be a valid positive number")
    private Long cardNumber;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Customer ID is required")
    @Min(value = 1, message = "Customer ID must be a valid positive number")
    private Integer customerId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be a positive value")
    private BigDecimal amount;
}
