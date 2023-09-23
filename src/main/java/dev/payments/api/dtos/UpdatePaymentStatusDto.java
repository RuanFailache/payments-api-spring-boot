package dev.payments.api.dtos;

import dev.payments.api.entities.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdatePaymentStatusDto(

        @NotNull
        UUID id,

        @NotNull
        PaymentStatus status

) {
}
