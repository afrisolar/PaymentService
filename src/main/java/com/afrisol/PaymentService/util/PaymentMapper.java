package com.afrisol.PaymentService.util;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.afrisol.PaymentService.dto.PaymentResponseDto;
import com.afrisol.PaymentService.model.Payment;

public class PaymentMapper {

    public static Payment mapToPaymentEntity(PaymentRequestDto dto) {
        return Payment.builder()
                .product(dto.getProduct())
                .quantity(dto.getQuantity())
                .cardNumber(dto.getCardNumber())
                .currency(dto.getCurrency())
                .customerId(dto.getCustomerId())
                .build();
    }

    public static PaymentResponseDto mapToPaymentResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .product(payment.getProduct())
                .quantity(payment.getQuantity())
                .cardNumber(payment.getCardNumber())
                .currency(payment.getCurrency())
                .customerId(payment.getCustomerId())
                .build();
    }
}
