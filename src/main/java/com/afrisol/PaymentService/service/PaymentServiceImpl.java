package com.afrisol.PaymentService.service;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.afrisol.PaymentService.dto.PaymentResponseDto;
import com.afrisol.PaymentService.exception.PaymentNotFoundException;
import com.afrisol.PaymentService.model.Payment;
import com.afrisol.PaymentService.repository.PaymentRepository;
import com.afrisol.PaymentService.util.PaymentMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    @Value("${topics.payment}")
    private String paymentsTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<PaymentResponseDto> processPayment(PaymentRequestDto paymentRequestDto) {
        log.info("Processing payment for Order: {}", paymentRequestDto);
        return paymentRepository.save(PaymentMapper.mapToPaymentEntity(paymentRequestDto))
                .doOnNext(savedPayment -> {
                    // Publish a message of successful payment
                    publishPaymentMessage(paymentRequestDto);
                    log.info("Successfully processed payment with ID: {} for request ID: {}",
                            savedPayment.getPaymentId(), paymentRequestDto);
                })
                .map(PaymentMapper::mapToPaymentResponseDto);
    }

    private void publishPaymentMessage(PaymentRequestDto paymentRequest) {
        try {
            String paymentMessage = objectMapper.writeValueAsString(paymentRequest);
            kafkaTemplate.send(paymentsTopic, paymentMessage); // Use the standalone topic name
            log.info("Published payment message to topic '{}': {}", paymentsTopic, paymentMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to publish payment message: {}", paymentRequest, e);
        }
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
                    existingPayment.setCustomerId(paymentRequestDto.getCustomerId());
                    return paymentRepository.save(existingPayment);
                })
                .doOnNext(updatedPayment ->
                        log.info("Successfully updated payment with ID: {} for request ID: {}", updatedPayment.getPaymentId(), requestID)
                )
                .map(PaymentMapper::mapToPaymentResponseDto);
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
        return paymentRepository.findAll().map(PaymentMapper::mapToPaymentResponseDto);
    }

    @Override
    public Mono<PaymentResponseDto> getPayment(Integer paymentId, String requestID) {
        if (paymentId == null) {
            return Mono.error(new IllegalArgumentException("Payment ID cannot be null"));
        }
        log.info("Searching for payment with ID: {}", paymentId);
        return paymentRepository.findById(Long.valueOf(paymentId))
                .switchIfEmpty(Mono.error(new PaymentNotFoundException("Payment not found with ID: " + paymentId)))
                .map(PaymentMapper::mapToPaymentResponseDto)
                .doOnNext(payment ->
                        log.info("Successfully retrieved payment with ID: {} for request ID: {}", paymentId, requestID)
                );
    }
}
