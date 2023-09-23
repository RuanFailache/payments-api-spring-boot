package dev.payments.api.dtos;

import dev.payments.api.entities.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CreatePaymentDto(

        @NotNull
        Long debitCode,

        @NotBlank
        @Pattern(regexp = "(\\d{11}|\\d{14})")
        String userIdentification,

        @NotNull
        PaymentMethod method,

        String cardNumber,

        @NotNull
        BigDecimal paymentValue

) {
}
