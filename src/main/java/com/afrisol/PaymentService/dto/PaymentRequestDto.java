package com.afrisol.PaymentService.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Customer name is required")
    private String customer;
}

