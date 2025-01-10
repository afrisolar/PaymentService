package com.afrisol.PaymentService.service;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.afrisol.PaymentService.dto.PaymentResponseDto;
import com.afrisol.PaymentService.model.Payment;
import com.afrisol.PaymentService.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment samplePayment;
    private PaymentRequestDto sampleRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        samplePayment = Payment.builder()
                .paymentId(1L)
                .product("ProductA")
                .quantity(2)
                .cardNumber(123456L)
                .currency("USD")
                .customerId(1024)
                .build();

        sampleRequestDto = PaymentRequestDto.builder()
                .product("ProductA")
                .quantity(2)
                .cardNumber(123456L)
                .currency("USD")
                .customerId(1024)
                .build();
    }

    @Test
    void addPayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(samplePayment));

        Mono<PaymentResponseDto> result = paymentService.processPayment(sampleRequestDto);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getProduct().equals("ProductA"))
                .verifyComplete();

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void updatePayment() {
        when(paymentRepository.findById(eq(1L))).thenReturn(Mono.just(samplePayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(Mono.just(samplePayment));

        Mono<PaymentResponseDto> result = paymentService.updatePayment(sampleRequestDto, 1, "req-123");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getCardNumber().equals(123456L))
                .verifyComplete();

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void deletePayment() {
        when(paymentRepository.findById(eq(1L))).thenReturn(Mono.just(samplePayment));
        when(paymentRepository.delete(any(Payment.class))).thenReturn(Mono.empty());

        Mono<Void> result = paymentService.deletePayment(1, "req-123");

        StepVerifier.create(result)
                .verifyComplete();

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).delete(any(Payment.class));
    }

    @Test
    void getAllPayments() {
        when(paymentRepository.findAll()).thenReturn(Flux.just(samplePayment));

        Flux<PaymentResponseDto> result = paymentService.getAllPayments("req-123");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getProduct().equals("ProductA"))
                .verifyComplete();

        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void getPayment() {
        when(paymentRepository.findById(eq(1L))).thenReturn(Mono.just(samplePayment));

        Mono<PaymentResponseDto> result = paymentService.getPayment(1, "req-123");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getPaymentId() == 1L &&
                                response.getProduct().equals("ProductA") &&
                                response.getQuantity() == 2 &&
                                response.getCardNumber() == 123456L &&
                                response.getCurrency().equals("USD") &&
                                response.getCustomerId() == 1024)
                .verifyComplete();

        verify(paymentRepository, times(1)).findById(1L);
    }
}
