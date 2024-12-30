package com.afrisol.PaymentService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    @NotBlank(message = "Product name is required")
    private String product;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Card number is required")
    @Min(value = 0, message = "Card number must be a valid positive number")
    private Long cardNumber;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Customer name is required")
    private String customer;
}
