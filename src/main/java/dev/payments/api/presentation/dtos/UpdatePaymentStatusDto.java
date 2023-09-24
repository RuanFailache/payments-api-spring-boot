package dev.payments.api.presentation.dtos;

import dev.payments.api.domain.entities.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePaymentStatusDto(

        @NotNull
        PaymentStatus status

) {
}
