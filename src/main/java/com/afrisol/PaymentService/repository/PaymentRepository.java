package com.afrisol.PaymentService.repository;

import com.afrisol.PaymentService.model.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PaymentRepository extends ReactiveCrudRepository<Payment, Long> {
}
