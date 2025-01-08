package com.afrisol.PaymentService.service;

import com.afrisol.PaymentService.dto.PaymentRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderConsumer {

    private final PaymentService paymentService;

    private final ObjectMapper objectMapper; // For JSON deserialization

    public OrderConsumer(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${topics.order}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Consumed message -> Key: {}, Value: {}, Partition: {}, Offset: {}",
                record.key(), record.value(), record.partition(), record.offset());

        try {
            // Convert JSON message to PaymentRequestDto
            PaymentRequestDto paymentRequest = objectMapper.readValue(record.value(), PaymentRequestDto.class);
            log.info("Converted PaymentRequestDto: {}", paymentRequest);

            // Pass the DTO to the service for processing
            paymentService.processPayment(paymentRequest);

        } catch (Exception e) {
            log.error("Failed to process message: {}", record.value(), e);
        }
    }
}
