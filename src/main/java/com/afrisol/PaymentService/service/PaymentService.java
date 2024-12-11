package com.afrisol.PaymentService.service;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.afrisol.PaymentService.dto.PaymentResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<PaymentResponseDto> addPayment(PaymentRequestDto paymentRequestDto, String requestID);
    Mono<PaymentResponseDto> updatePayment(PaymentRequestDto paymentRequestDto, Integer paymentId, String requestID);
    Mono<Void> deletePayment(Integer paymentId, String requestID);
    Flux<PaymentResponseDto> getAllPayments(String requestID);
    Mono<PaymentResponseDto> getPayment(Integer paymentId, String requestID);
}
