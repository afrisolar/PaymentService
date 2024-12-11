package com.afrisol.PaymentService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("payment")
public class Payment {
    @Id
    private Long paymentId;
    private String product; // Product represented as a simple string
    private Integer quantity;
    private String paymentMethod;
    private String currency; // e.g., USD, EUR
    private String customer; // Customer represented as a simple string (e.g., name)

    public Payment(String product, int quantity, BigDecimal unitPrice, String paymentMethod, String currency, String customer) {
        this.product = product;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
    }

    public void processPayment() {
        BigDecimal totalAmount = BigDecimal.valueOf(quantity * 10L);
    }
}

