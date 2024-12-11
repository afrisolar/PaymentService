package com.afrisol.PaymentService.controller;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.afrisol.PaymentService.dto.PaymentResponseDto;
import com.afrisol.PaymentService.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private WebTestClient webTestClient;

    private PaymentResponseDto samplePaymentResponse;
    private PaymentRequestDto samplePaymentRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize WebTestClient
        webTestClient = WebTestClient.bindToController(paymentController).build();

        // Sample PaymentResponseDto
        samplePaymentResponse = PaymentResponseDto.builder()
                .paymentId(1L)
                .product("ProductA")
                .quantity(2)
                .totalAmount(BigDecimal.valueOf(200))
                .paymentMethod("CreditCard")
                .currency("USD")
                .customer("Customer1")
                .build();

        // Sample PaymentRequestDto
        samplePaymentRequest = PaymentRequestDto.builder()
                .product("ProductA")
                .quantity(2)
                .paymentMethod("CreditCard")
                .currency("USD")
                .customer("Customer1")
                .build();
    }

    @Test
    void getAllPayments() {
        when(paymentService.getAllPayments(anyString())).thenReturn(Flux.just(samplePaymentResponse));

        webTestClient.get()
                .uri("/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PaymentResponseDto.class)
                .hasSize(1)
                .consumeWith(response -> {
                    PaymentResponseDto payment = response.getResponseBody().get(0);
                    assertEquals("ProductA", payment.getProduct());
                });

        verify(paymentService, times(1)).getAllPayments(anyString());
    }

    @Test
    void getPaymentById() {
        when(paymentService.getPayment(eq(1), anyString())).thenReturn(Mono.just(samplePaymentResponse));

        webTestClient.get()
                .uri("/api/v1/payments/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentResponseDto.class)
                .consumeWith(response -> {
                    PaymentResponseDto payment = response.getResponseBody();
                    assertEquals("ProductA", payment.getProduct());
                });

        verify(paymentService, times(1)).getPayment(eq(1), anyString());
    }

    @Test
    void createPayment() {
        when(paymentService.addPayment(any(PaymentRequestDto.class), anyString())).thenReturn(Mono.just(samplePaymentResponse));

        webTestClient.post()
                .uri("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(samplePaymentRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentResponseDto.class)
                .consumeWith(response -> {
                    PaymentResponseDto payment = response.getResponseBody();
                    assertEquals("ProductA", payment.getProduct());
                });

        verify(paymentService, times(1)).addPayment(any(PaymentRequestDto.class), anyString());
    }

    @Test
    void updatePayment() {
        when(paymentService.updatePayment(any(PaymentRequestDto.class), eq(1), anyString())).thenReturn(Mono.just(samplePaymentResponse));

        webTestClient.put()
                .uri("/api/v1/payments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(samplePaymentRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentResponseDto.class)
                .consumeWith(response -> {
                    PaymentResponseDto payment = response.getResponseBody();
                    assertEquals("ProductA", payment.getProduct());
                });

        verify(paymentService, times(1)).updatePayment(any(PaymentRequestDto.class), eq(1), anyString());
    }

    @Test
    void deletePayment() {
        when(paymentService.deletePayment(eq(1), anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/payments/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(paymentService, times(1)).deletePayment(eq(1), anyString());
    }
}
