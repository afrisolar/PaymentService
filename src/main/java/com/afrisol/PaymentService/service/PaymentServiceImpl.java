package com.afrisol.PaymentService.service;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.afrisol.PaymentService.dto.PaymentResponseDto;
import com.afrisol.PaymentService.exception.PaymentNotFoundException;
import com.afrisol.PaymentService.model.Payment;
import com.afrisol.PaymentService.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Mono<PaymentResponseDto> addPayment(PaymentRequestDto paymentRequestDto, String requestID) {
        if (paymentRequestDto == null) {
            return Mono.error(new IllegalArgumentException("PaymentRequestDto cannot be null"));
        }
        return paymentRepository.save(mapToPaymentEntity(paymentRequestDto))
                .doOnNext(savedPayment ->
                        log.info("Successfully added payment with ID: {} for request ID: {}", savedPayment.getPaymentId(), requestID)
                )
                .map(this::mapToPaymentResponseDto);
    }

    @Override
    public Mono<PaymentResponseDto> updatePayment(@Valid PaymentRequestDto paymentRequestDto, Integer paymentId, String requestID) {
        log.info("Updating payment with ID: {} Request ID: {}", paymentId, requestID);

        return paymentRepository.findById(Long.valueOf(paymentId))
                .switchIfEmpty(Mono.error(new PaymentNotFoundException("Payment not found with ID: " + paymentId)))
                .flatMap(existingPayment -> {
                    // Update payment fields
                    existingPayment.setProduct(paymentRequestDto.getProduct());
                    existingPayment.setQuantity(paymentRequestDto.getQuantity());
                    existingPayment.setCardNumber(paymentRequestDto.getCardNumber());
                    existingPayment.setCurrency(paymentRequestDto.getCurrency());
                    existingPayment.setCustomer(paymentRequestDto.getCustomer());
                    return paymentRepository.save(existingPayment);
                })
                .doOnNext(updatedPayment ->
                        log.info("Successfully updated payment with ID: {} for request ID: {}", updatedPayment.getPaymentId(), requestID)
                )
                .map(this::mapToPaymentResponseDto);
    }

    @Override
    public Mono<Void> deletePayment(Integer paymentId, String requestID) {
        log.info("Deleting payment with ID: {} Request ID: {}", paymentId, requestID);
        if (paymentId == null) {
            return Mono.error(new IllegalArgumentException("Invalid payment ID"));
        }
        return paymentRepository.findById(Long.valueOf(paymentId))
                .switchIfEmpty(Mono.error(new PaymentNotFoundException("Payment not found with ID: " + paymentId)))
                .flatMap(paymentRepository::delete)
                .doOnSuccess(unused -> log.info("Successfully deleted payment with ID: {} for request ID: {}", paymentId, requestID));
    }

    @Override
    public Flux<PaymentResponseDto> getAllPayments(String requestID) {
        log.info("Retrieving all payments for request ID: {}", requestID);
        return paymentRepository.findAll().map(this::mapToPaymentResponseDto);
    }

    @Override
    public Mono<PaymentResponseDto> getPayment(Integer paymentId, String requestID) {
        if (paymentId == null) {
            return Mono.error(new IllegalArgumentException("Payment ID cannot be null"));
        }
        log.info("Searching for payment with ID: {}", paymentId);
        return paymentRepository.findById(Long.valueOf(paymentId))
                .switchIfEmpty(Mono.error(new PaymentNotFoundException("Payment not found with ID: " + paymentId)))
                .map(this::mapToPaymentResponseDto)
                .doOnNext(payment ->
                        log.info("Successfully retrieved payment with ID: {} for request ID: {}", paymentId, requestID)
                );
    }

    private Payment mapToPaymentEntity(PaymentRequestDto dto) {
        return Payment.builder()
                .product(dto.getProduct())
                .quantity(dto.getQuantity())
                .cardNumber(dto.getCardNumber())
                .currency(dto.getCurrency())
                .customer(dto.getCustomer())
                .build();
    }

    private PaymentResponseDto mapToPaymentResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .product(payment.getProduct())
                .quantity(payment.getQuantity())
                .cardNumber(payment.getCardNumber())
                .currency(payment.getCurrency())
                .customer(payment.getCustomer())
                .build();
    }
}
