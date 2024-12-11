package com.afrisol.PaymentService.model;

import java.math.BigDecimal;

// PaymentMethod abstract class
abstract class PaymentMethod {
    public abstract void processPayment(BigDecimal amount);
}

