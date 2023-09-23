package dev.payments.api.dtos;

import dev.payments.api.entities.Payment;
import dev.payments.api.entities.PaymentMethod;
import dev.payments.api.entities.PaymentStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record PaymentDto(
        UUID id,
        Long debitCode,
        String userIdentification,
        PaymentMethod method,
        PaymentStatus status,
        BigDecimal value
) {

    public PaymentDto(Payment payment) {
        this(
                payment.getId(),
                payment.getDebitCode(),
                payment.getUserIdentification(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getPaymentValue()
        );
    }

}
