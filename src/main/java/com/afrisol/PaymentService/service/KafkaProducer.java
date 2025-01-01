package com.afrisol.PaymentService.service;

import com.afrisol.PaymentService.dto.PaymentResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private static final String TOPIC = "payment";

    private final KafkaTemplate<String, PaymentResponseDto> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, PaymentResponseDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(PaymentResponseDto paymentResponseDto) {
        log.info("Publishing message to Kafka topic: {}", paymentResponseDto);
        kafkaTemplate.send(TOPIC, paymentResponseDto);
    }
}
