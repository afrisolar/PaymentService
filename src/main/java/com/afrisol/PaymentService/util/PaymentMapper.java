package com.afrisol.PaymentService.util;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.afrisol.PaymentService.dto.PaymentResponseDto;
import com.afrisol.PaymentService.model.Payment;

public class PaymentMapper {

    public static Payment mapToPaymentEntity(PaymentRequestDto dto) {
        return Payment.builder()
                .product(dto.getProduct())
                .orderNumber(dto.getOrderNumber())
                .quantity(dto.getQuantity())
                .cardNumber(dto.getCardNumber())
                .currency(dto.getCurrency())
                .customerId(dto.getCustomerId())
                .amount(dto.getAmount())
                .build();
    }

    public static PaymentResponseDto mapToPaymentResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .product(payment.getProduct())
                .orderNumber(payment.getOrderNumber())
                .quantity(payment.getQuantity())
                .amount(payment.getAmount())
                .cardNumber(payment.getCardNumber())
                .currency(payment.getCurrency())
                .customerId(payment.getCustomerId())
                .build();
    }
}
