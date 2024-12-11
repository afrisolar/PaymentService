package com.afrisol.PaymentService.model;

import java.math.BigDecimal;

// PayPal class
class PayPal extends PaymentMethod {
    private String email;

    public PayPal(String email) {
        this.email = email;
    }

    @Override
    public void processPayment(BigDecimal amount) {
        System.out.println("Processing " + amount + " payment via PayPal for email " + email);
    }
}