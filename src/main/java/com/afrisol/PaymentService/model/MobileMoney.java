package com.afrisol.PaymentService.model;

import java.math.BigDecimal;

// MobileMoney class
class MobileMoney extends PaymentMethod {
    private String providerName; // e.g., MTN, Airtel, UTL
    private String phoneNumber;

    public MobileMoney(String providerName, String phoneNumber) {
        this.providerName = providerName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void processPayment(BigDecimal amount) {
        System.out.println("Processing " + amount + " payment via " + providerName + " for number " + phoneNumber);
    }
}
