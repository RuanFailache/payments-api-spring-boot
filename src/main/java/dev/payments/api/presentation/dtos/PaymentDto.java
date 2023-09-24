package dev.payments.api.presentation.dtos;

import dev.payments.api.domain.entities.Payment;
import dev.payments.api.domain.entities.PaymentMethod;
import dev.payments.api.domain.entities.PaymentStatus;

import java.math.BigDecimal;
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
