package com.afrisol.PaymentService.controller;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.afrisol.PaymentService.dto.PaymentResponseDto;
import com.afrisol.PaymentService.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public Flux<PaymentResponseDto> getAllPayments() {
        String requestID = UUID.randomUUID().toString();
        log.info("Retrieving all payments: {}", requestID);
        return paymentService.getAllPayments(requestID);
    }

    @GetMapping("/{paymentId}")
    public Mono<ResponseEntity<PaymentResponseDto>> getPaymentById(@PathVariable Integer paymentId) {
        String requestID = UUID.randomUUID().toString();
        log.info("Retrieving payment with ID: {} and request ID: {}", paymentId, requestID);
        return paymentService.getPayment(paymentId, requestID)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{paymentId}")
    public Mono<ResponseEntity<PaymentResponseDto>> updatePayment(@PathVariable Integer paymentId, @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        String requestID = UUID.randomUUID().toString();
        log.info("Updating payment with ID: {} and request ID: {}", paymentId, requestID);
        return paymentService.updatePayment(paymentRequestDto, paymentId, requestID)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{paymentId}")
    public Mono<ResponseEntity<Object>> deletePayment(@PathVariable Integer paymentId) {
        String requestID = UUID.randomUUID().toString();
        log.info("Deleting payment with ID: {} and request ID: {}", paymentId, requestID);
        return paymentService.deletePayment(paymentId, requestID)
                .then(Mono.just(ResponseEntity.noContent().<Object>build()));
    }
}
