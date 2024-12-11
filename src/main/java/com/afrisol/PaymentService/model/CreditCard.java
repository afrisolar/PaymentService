package com.afrisol.PaymentService.model;

import java.math.BigDecimal;

// CreditCard class
class CreditCard extends PaymentMethod {
    private String cardType; // e.g., Visa, MasterCard, etc.
    private String cardNumber;

    public CreditCard(String cardType, String cardNumber) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
    }

    @Override
    public void processPayment(BigDecimal amount) {
        System.out.println("Processing " + amount + " payment with " + cardType + " card ending in " + cardNumber.substring(cardNumber.length() - 4));
    }
}