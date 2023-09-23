package dev.payments.api.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeletePaymentDto(@NotNull UUID id) {
}
