package com.afrisol.PaymentService.service;

import com.afrisol.PaymentService.dto.PaymentResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topics = "payment", groupId = "payment-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(PaymentResponseDto paymentResponseDto) {
        // Log the received message
        log.info("Received message from topic 'payment': {}", paymentResponseDto);

        // Print the payment details to the console
        System.out.println("Payment Details: " + paymentResponseDto);
    }
}